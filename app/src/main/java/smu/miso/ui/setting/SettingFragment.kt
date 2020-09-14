package smu.miso.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_setting.*
import smu.miso.CloudFunctions
import smu.miso.MainActivity
import smu.miso.R

class SettingFragment : Fragment() {
    //전역 변수 설정 공간
    private lateinit var settingViewModel: SettingViewModel

    private var auth = FirebaseAuth.getInstance()
    private var uid = auth.uid
    private var user = auth.currentUser
    private var userRef = FirebaseDatabase.getInstance().reference

    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지
        settingViewModel =
            ViewModelProviders.of(this).get(SettingViewModel::class.java)
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_setting, container, false)
        return root
    }

    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var emailVerified = ""
        var department = ""
        var studentId = ""
        //현재 로그인 된 회원 정보 가져오기
        //DB root->users->studentID 참조하여 회원정보 가져오기
        uid?.let {
            userRef.child("users").child(it)
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                        studentId = datasnapshot.child("studentId").value.toString()
                        emailVerified = datasnapshot.child("emailVerified").value.toString()
                        department = datasnapshot.child("department").value.toString()

                        emailValue?.text = "$studentId@sangmyung.kr"
                        if (emailVerified.toBoolean()) {
                            emailVerifiedValue?.text = "인증 완료"
                        } else {
                            emailVerifiedValue?.text = "인증 실패"
                        }

                        uidValue?.text = uid
                        departmentValue?.text = department
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.w("READ FAILED", "Failed to read value")
                    }
                })
        }

        //사용자 계정 로그아웃 함수
        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            //fragment에서 toast 전송
            Toast.makeText(
                context,
                "로그아웃 되었습니다. 다시 로그인 해주세요",
                Toast.LENGTH_SHORT
            ).show()
            //fragment에서 activity 종료
            activity?.finish()
        }

        //사용자 삭제 함수
        signoutBtn.setOnClickListener {
            if (reAuthStudentId.text.toString() == "" && reAuthPassword.text.toString() == "") {
                Toast.makeText(
                    activity, "학번과 비밀번호를 입력 후 탈퇴버튼을 터치",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //TODO: 1. 사용자 인증 후 Authentication에서 삭제, 2. 데이터베이스에서 사용자 정보 삭제
                //uid를 auth.currentUser 에서 받아오기 때문에 reAuthenticate() 함수 호출 시 uid가 소멸됨. deleteUid에 소멸 전에 받아둠.
                //Authentication, Database에서 정보 삭제 확인
                val deleteUid = uid
                reAuthenticate()
                userRef.child("users").child(deleteUid.toString()).setValue(null)
            }
        }

        setting.setOnClickListener {
            CloudFunctions.hideKeyboard(context, this.view)
        }

    }

    private fun deleteUser() {
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.signOut()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(context, "아이디 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()
                activity?.finish()

            } else {
                Toast.makeText(
                    activity,
                    "사용자 계정 삭제 실패",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    //사용자 재인증 과정
    private fun reAuthenticate() {
        Log.e("repassword", user?.email.toString() + " : " + reAuthPassword.text.toString())
        val credential = EmailAuthProvider.getCredential(
            reAuthStudentId.text.toString() + "@sangmyung.kr",
            reAuthPassword.text.toString()
        )
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    deleteUser()
                } else {
                    Toast.makeText(
                        activity,
                        "학번 혹은 패스워드를 다시 확인해 주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
}