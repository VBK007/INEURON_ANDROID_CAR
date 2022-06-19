package nr.king.carserv.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nr.king.carserv.R
import nr.king.carserv.common.getBitmapFromDrawable


class MyFirebaseMessagingService : FirebaseMessagingService() {

    var notificationId = 101
    var dataMessageId = 102

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.data.isNotEmpty().let {
            sendNotification(remoteMessage, dataMessageId)
        }

        remoteMessage.notification?.let {
            sendNotification(remoteMessage, notificationId)
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {

    }

    private fun sendNotification(messageBody: RemoteMessage, msgId: Int) {
        val channelId = "GFTChannel"
        var title = getString(R.string.app_name)
        var notififyId = 1001
        var message = ""
        when (msgId) {
            notificationId -> {
                title = messageBody.notification!!.title!!
                message = messageBody.notification!!.body!!
            }
            dataMessageId -> {
                message = messageBody.data["body"]!!
                title = messageBody.data["title"]!!
                notififyId = Integer.parseInt(messageBody.data["not_id"]!!)
            }
        }

     /*   val intent = Intent(this, Splashscreen::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)*/

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(message)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentText(message)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.carservice)
            .setColor(ContextCompat.getColor(this, R.color.colorBlues))
            .setLargeIcon(this.getBitmapFromDrawable(R.drawable.carservice))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setStyle(bigText)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Delivery Schedule Notification Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(getString(R.string.app_name), notififyId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
