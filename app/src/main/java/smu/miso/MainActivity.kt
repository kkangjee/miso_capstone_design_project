package smu.miso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import smu.miso.signup.SignUpActivity
import smu.miso.signup.VerifyEmailActivity
import smu.miso.signup.verifiedEmail

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

        //자동 로그인 기능.
        //Firebase 에 User 정보가 이미 존재 하면 바로 HomeActivity 로 이동
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
        //배경을 누르면 키보드가 내려가게끔
        mainview.setOnClickListener{
            CloudFunctions.hideKeyboard(this)
        }

        //회원가입 정보가 없을 때 회원가입 액티비티로 이동
        bt_gotoSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        //로그인 버튼 눌러 서비스 로그인
        bt_login.setOnClickListener {

            if (inputStudentID.text.toString().isEmpty() || inputPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "학번 혹은 비밀번호를 반드시 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(
                    inputStudentID.text.toString() + "@sangmyung.kr",
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

    //뒤로가기 버튼 2번 터치 시 액티비티 종료 함수
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        CloudFunctions.hideKeyboard(this)
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
}