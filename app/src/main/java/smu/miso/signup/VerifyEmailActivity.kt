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
import kotlinx.android.synthetic.main.activity_verify_email.*
import smu.miso.CloudFunctions
import smu.miso.signup.verifiedEmail
import smu.miso.MainActivity
import smu.miso.R
import smu.miso.model.UserModel


class VerifyEmailActivity : AppCompatActivity() {

    private val TAG = "email"
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val userRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)
        supportActionBar?.hide()

        //SignUpEmailActivity 에서 학과 명 받아오기
        val department: String? = intent.getStringExtra("department")

        val uid: String = user?.uid.toString()
        //val email: String? = user?.email

        var studentID = intent.getStringExtra("studentID")
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

        //이메일 인증까지 완료 시 MainActivity로 넘어가 로그인하도록 설정
        bt_gotoMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //학교 이메일로 인증된 사용자인지 확인
        Confirm.setOnClickListener {
            if (auth == null) {  //사용자 없을시
                Toast.makeText(
                    this@VerifyEmailActivity,
                    "사용자가 없습니다. 완전 오류",
                    Toast.LENGTH_SHORT
                ).show()
            } else { // 사용자 있을 시 다시 불러옴
                user?.reload()
                    ?.addOnSuccessListener {
                    }
            }
            when (verifiedEmail()) {
                true -> {
                    userIsVerified.setText(R.string.email_verified)
                    userIsVerified.setTextColor(Color.parseColor("#4CAF50"))

                    Confirm.text = "완료"
                    bt_gotoMain.visibility = View.VISIBLE

                    Toast.makeText(
                        this@VerifyEmailActivity,
                        "계정 생성과 이메일 인증이 모두 완료되었습니다. 새로 로그인 하십시오",
                        Toast.LENGTH_SHORT
                    ).show()

                    //이메일 인증 까지 완료 되었으면 DB에 USER 객체 생성(emailVerfied 값을 true 로 DB에 전달)
                    //기본 태그는 학과명으로 태그 설정
                    if (studentID != null) {
                        userRef.child("users").child(uid)
                            .setValue(UserModel(uid, studentID, true, department, null))
                            .addOnSuccessListener {
                                Log.d("USER_INSERTED!!", "데이터베이스에 User 객체 생성 완료")
                            }.addOnFailureListener {
                                Log.d("USER_INSERTED_FAIL!!", "데이터베이스에 User 객체 생성 실패")
                            }
                    }
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

        verify_email.setOnClickListener{
            CloudFunctions.hideKeyboard(this)
        }
    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        user?.delete()
        val intent = Intent(this, SignUpEmailActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        user?.delete()
        super.onStop()
    }
}