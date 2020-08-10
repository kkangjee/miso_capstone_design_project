package smu.miso.signup

import com.google.firebase.auth.FirebaseAuth

private val auth = FirebaseAuth.getInstance()
private val user = auth.currentUser

//이메일 인증 성공여부 판별
fun verifiedEmail(): Boolean {
    user?.let {
        // Check if user's email is verified'
        //Boolean Type
        user.isEmailVerified
    }
    return user!!.isEmailVerified
}

