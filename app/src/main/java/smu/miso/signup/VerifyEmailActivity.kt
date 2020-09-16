package smu.miso.signup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import kotlinx.android.synthetic.main.activity_verify_email.*
import smu.miso.CloudFunctions
import smu.miso.MainActivity
import smu.miso.R
import smu.miso.model.UserModel


class VerifyEmailActivity : AppCompatActivity() {

    private val TAG = "email"
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val userRef = FirebaseDatabase.getInstance().reference
    private val token = FirebaseInstanceId.getInstance().getToken()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)
        supportActionBar?.hide()

        val studentID = intent.getStringExtra("studentID")
        val department = intent.getStringExtra("department")

        if (!studentID.isNullOrEmpty()) {
            studentIDText.setText(studentID)
        } else {
            Toast.makeText(
                this@VerifyEmailActivity,
                "학번을 입력해주세요.",
                Toast.LENGTH_LONG
            ).show()
        }

        //인증메일 발송 버튼 누를 시 해당 메일로 인증 메일 전송.
        sndEmail.setOnClickListener {
            user?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    sndEmail.text = "인증메일 재발송"
                    if (task.isSuccessful) {
                        Log.d(TAG, "Verified Email sent.")
                        Toast.makeText(
                            this@VerifyEmailActivity,
                            "계정 등록에 성공했습니다. 해당 이메일로 접속하여 링크에 들어가 학생 인증을 해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.d(TAG, "Email sent Failed.")
                        Toast.makeText(
                            this@VerifyEmailActivity,
                            "검증 이메일을 보내는 것에 실패 했습니다",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        userIsVerified.setText(R.string.no_email_verified)
        userIsVerified.setTextColor(Color.parseColor("#F44336"))

        //이메일 인증단계를 끝냈으면 태그 선택 액티비티로 이동
        bt_gotoSelectTag.setOnClickListener {
            val intent = Intent(this, SelectTagActivity::class.java)
            intent.putExtra("studentID",studentIDText.text.toString())
            intent.putExtra("department", department)
            startActivity(intent)
            finish()
        }

        //학교 이메일로 인증된 사용자인지 확인
        Confirm.setOnClickListener {
            user?.reload()
                ?.addOnSuccessListener {
                    Log.d("학교 이메일 인증", "완료")
                }
            when (verifiedEmail()) {
                true -> {
                    userIsVerified.setText(R.string.email_verified)
                    userIsVerified.setTextColor(Color.parseColor("#4CAF50"))

                    Confirm.text = "완료"
                    bt_gotoSelectTag.visibility = View.VISIBLE

                    Toast.makeText(
                        this@VerifyEmailActivity,
                        "계정 생성과 이메일 인증이 완료되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    Toast.makeText(
                        this@VerifyEmailActivity,
                        "사용자 정보를 업데이트 중 입니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Confirm.text = "재 확인"
                }
            }
        }

        verify_email.setOnClickListener {
            CloudFunctions.hideKeyboard(this)
        }
    }
}