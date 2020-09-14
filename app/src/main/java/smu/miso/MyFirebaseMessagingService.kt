package smu.miso

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import smu.miso.Chat.ChatActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    override fun onNewToken(token : String) {
        Log.d(TAG, "Refreshed token : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            val title = remoteMessage.data.get("title").toString()
            val body = remoteMessage.data.get("body").toString()
            sendNotification(title,body)
        }
        //추후에 이 title을 가지고 sendNotification 에서 ChatActivity를 intent 할 지,
        //TagChatActivity를 intent 할 지 정할것임!!!!
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun sendNotification(messageTitle : String, messageBody: String) {
        Log.d("됐는가",messageBody)
        val NOTIFICATION_ID : Int = 1001
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_MAX,
            false, "Example", "App notification channel") // 1
        //getString(R.string.app_name) 이게 channel ID
        val channelId = "$packageName-${"Example"}" // 2
        val title = messageTitle //알람제목
        val content = messageBody //알람내용

        val intent = Intent(baseContext, ChatActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 3

        val builder = NotificationCompat.Builder(this, channelId)  // 4
        builder.setSmallIcon(R.drawable.ic_dashboard_black_24dp)    // 5
        builder.setContentTitle(title)    // 6
        builder.setContentText(content)    // 7
        builder.priority = NotificationCompat.PRIORITY_HIGH    // 8
        builder.setAutoCancel(true)   // 9
        builder.setContentIntent(pendingIntent)   // 10

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())    // 11
    }
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                  name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}

