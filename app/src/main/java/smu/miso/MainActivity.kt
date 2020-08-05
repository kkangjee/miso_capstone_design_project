package smu.miso

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import smu.miso.signup.SignUpActivity
import smu.miso.signup.VerifyEmailActivity

class MainActivity : AppCompatActivity() {
    var mBackWait: Long = 0

    //이메일 비밀번호 로그인 모듈 변수
    private val auth = FirebaseAuth.getInstance()

    //현재 로그인 된 유저 정보를 담을 변수
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if (user != null) {
            when (verifiedEmail()) {
                true -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        baseContext, "로그인에 성공했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                false -> {
                    val intent = Intent(this, VerifyEmailActivity::class.java)

                    //저장되어있는 유저 정보에서 이메일을 가져와 다음 액티비티로 보내기
                    val modify_email = user.email.toString().split("@")
                    intent.putExtra("studentID", modify_email[0])
                    startActivity(intent)
                }
            }

        }


        //회원가입 정보가 없을 때 텍스트 눌러 회원가입 액티비티로 이동
        bt_gotoSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        //로그인 버튼 눌러 서비스 로그인
        bt_login.setOnClickListener {
            if (inputEmail.text.toString().isEmpty() || inputPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "학번 혹은 비밀번호를 반드시 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(
                    inputEmail.text.toString() + "@sangmyung.kr",
                    inputPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "signInWithEmailPassword : 성공")
                            Toast.makeText(
                                baseContext, "로그인에 성공했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("TAG", "signInWithEmailPassword : 실패")
                            Toast.makeText(
                                baseContext,
                                "존재하지 않는 계정이거나, 로그인에 실패했습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    //뒤로가기 버튼 2번 터치 시 액티비티 종료
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(
                this@MainActivity,
                "뒤로가기 버튼을 한번 더 누르면 종료됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            finish()
        }
    }


    private fun verifiedEmail(): Boolean {
        user?.let {
            // Check if user's email is verified'
            //Boolean Type
            user.isEmailVerified
        }
        return user!!.isEmailVerified
    }
}