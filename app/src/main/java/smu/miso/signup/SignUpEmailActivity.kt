package smu.miso.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import smu.miso.CloudFunctions
import smu.miso.MainActivity
import smu.miso.R


class SignUpEmailActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)
        supportActionBar?.hide()
        //SignUpActivity 에서 학교 명 받아오기
        val univName = intent.getStringExtra("univName")
        rcv_univName.text = univName

        val deptName = intent.getStringExtra("department")
        rcv_deptName.text = deptName

        //제출하기 버튼을 눌렀을때
        Submit2.setOnClickListener {
            val email = studentID.text.toString() + "@sangmyung.kr"
            val password = inputPwd.text.toString()
            val re_password = re_inputPwd.text.toString()

            //기존 가입된 사용자 이메일 정보를 가져와 신규 가입자와 동일한 이메일인지 확인하기 위한 user 객체 생성
            if (password != re_password) {
                Toast.makeText(
                    this@SignUpEmailActivity,
                    "입력한 비밀번호와 다릅니다. 다시 확인하세요.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                createEmailID(email, password)
            }
        }
        sign_up_email.setOnClickListener{
            CloudFunctions.hideKeyboard(this)
        }
    }

    private fun createEmailID(email: String, password: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password) //이게 Auth에 지정해주는 곳
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveNextPage()
                }
                else {
                    val currentUser = auth.currentUser
                    //auth는 등록됐고, email인증이 안 됐다면, 이메일 인증 화면으로 다시 가게끔
                    if(!verifiedEmail() && currentUser != null){
                        currentUser.updatePassword(password)
                        moveNextPage()
                    }
                    //auth도 있고, email인증도 된 경우
                    else {
                        Toast.makeText(
                            this@SignUpEmailActivity,
                            "이미 가입한 학번 입니다. 다시 확인해 주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    //로그인 세션 관리, SignUp이 정상적으로 완료되면,
    private fun moveNextPage() {
        //파이어베이스 인증 인스턴스 초기화 후 현재 파이어베이스에 등록되어 있고, 로그인된 사용자라면 이메일 인증 액티비티로 전환
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, VerifyEmailActivity::class.java)
            //입력했던 학번 다음 액티비티로 보내기
            intent.putExtra("studentID", studentID.text.toString())
            intent.putExtra("department", rcv_deptName.text.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}