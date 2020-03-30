package co.beulahana

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import co.beulahana.notificationapp.MainActivity
import co.beulahana.notificationapp.R

/**
 * Created by Sandra Konotey on 2020-02-10.
 */
object  NotificationUtility {
    private val NOTIFICATION_ID = 0

    val ACTION_UPDATE_NOTIFICATION =
        "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION"
    val ACTION_DISMISS_NOTIFICATION =
        "com.example.android.notifyme.ACTION_DISMISS_NOTIFICATION"

    private val CHANNEL_ID="primary_channel_id"
    lateinit var notificationManager:NotificationManager


    fun createNotificationChannel(context: Context){
        notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"Test Notification", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.description="I am a test notification"
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor= Color.RED
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun getNotificationBuilder(activity: Context): NotificationCompat.Builder{
        val builder =  NotificationCompat.Builder(activity,CHANNEL_ID)
            .setContentTitle("AAC Test")
            .setContentText("Ready?")
            .setSmallIcon(R.drawable.ic_android)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        builder.priority = NotificationCompat.PRIORITY_HIGH

        return builder
    }

    fun sendNotification(activity:Context){
        val notificationBuilder = getNotificationBuilder(activity)

        //action to update intent
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(
            activity,NOTIFICATION_ID,updateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.addAction(R.drawable.ic_update,"Update Action",updatePendingIntent)

        //action to invoke when notification is dismissed
        val deleteIntent = Intent(ACTION_DISMISS_NOTIFICATION)
        val deletePendingIntent = PendingIntent.getBroadcast(activity,NOTIFICATION_ID,deleteIntent,
            PendingIntent.FLAG_ONE_SHOT)
        notificationBuilder.setDeleteIntent(deletePendingIntent)


        val intent = Intent(activity, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(activity,NOTIFICATION_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setAutoCancel(true)
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }



     fun updateNotification(activity: Context){
        val image = BitmapFactory.decodeResource(activity.resources,R.drawable.mascot_1)
        val notificationBuilder = getNotificationBuilder(activity).setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(image)
            .setBigContentTitle("Big Content Title"))
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }


     fun cancelNotification(){
        notificationManager.cancel(NOTIFICATION_ID)
    }

}