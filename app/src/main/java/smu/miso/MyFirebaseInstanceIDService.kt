package smu.miso

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService  : FirebaseInstanceIdService(){
    override fun onTokenRefresh(){
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        Log.d(ContentValues.TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken)
        sendRegistrationToServer()
    }

    fun sendRegistrationToServer() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val token = FirebaseInstanceId.getInstance().getToken()
        FirebaseDatabase.getInstance().reference.child("users").child(uid).child("token").setValue(
            token
        )
    }
}