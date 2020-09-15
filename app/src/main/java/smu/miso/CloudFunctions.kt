package smu.miso

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object CloudFunctions {

    fun hideKeyboard(mActivity: Activity) { //Activity
        val view = mActivity.currentFocus
        if (view != null) {
            val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideKeyboard(context: Context?, view: View?) { //Fragment
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }


    //TITLE과 Body 확인하세요
    fun sendFCM(token: String) {
        Thread(Runnable() {
            try {
                var API_URL_FCM: String = "https://fcm.googleapis.com/fcm/send"
                //현재 접속중인 기기의 Token
                var Token: String = token
                //우리 앱 서버 고유키 값
                var AUTH_KEY_FCM: String =
                    "AAAApjdbQG4:APA91bE1m9d1DN1mxSPLrwy3hby3k8_MDHLfPC6NFBewCP8bcklI8bJfkSg8sHgGCwOo3fqqsH-07YegNowRfJfWwl6baiK0PcdhcmtaKJt2e9OwYUtu7cjR61tmrmeaSZYaaQ8OLoxn"
                val Url: URL = URL("https://fcm.googleapis.com/fcm/send")
                val conn: HttpURLConnection = Url.openConnection() as HttpURLConnection
                conn.doOutput = true
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM)

                conn.doOutput = true

                val json: JSONObject = JSONObject()
                val notification: JSONObject = JSONObject()

                //title이 알람제목, body가 알람내용
                //notification(아이콘, 알람소리 등)을 수정하고 싶으면 MyFirebaseMessagingservice를 수정해야함함
                notification.put("title", "알람")
                notification.put("body", "방 생성이 완료됐음")

                json.put("to", Token)
                json.put("direct_book_ok", true)
                json.put("data", notification)
                json.put("priority", "high")

                val sendMsg = json.toString()
                //아래의 로그는 보낸 메세지 쿼리문을 볼 수 있음
                Log.d("sendMessage", sendMsg);
                val os: OutputStream = conn.outputStream
                os.write(sendMsg.toByteArray(charset("utf-8")))
                os.flush()
                os.close()

                val responseCode = conn.responseCode
                //응답받은 코드 200이면 정상, 나머지면 오류
                //응답코드 및 Json 필드변수 : https://firebase.google.com/docs/cloud-messaging/http-server-ref?authuser=0#table9 참고
                Log.d("response", responseCode.toString())


                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                Log.d("responseMessage", response.toString())
                `in`.close()

                //{
                // "data": {
//                    "score": "5x1",
//                    "time": "15:10"
//                },
//                    "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
//                    "direct_book_ok" : true
//                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("asdf", "asdf");
            }
        }).start()
    }
}