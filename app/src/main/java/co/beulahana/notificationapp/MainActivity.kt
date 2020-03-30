package co.beulahana.notificationapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.beulahana.NotificationUtility
import co.beulahana.NotificationUtility.ACTION_DISMISS_NOTIFICATION
import co.beulahana.NotificationUtility.ACTION_UPDATE_NOTIFICATION
import co.beulahana.jobscheduler.NotificationJobService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {




    private val notifyReceiver = NotifyReceiver()
    private lateinit var  jobScheduler:JobScheduler
    private val JOB_ID = 0
    var selectedNetwork = JobInfo.NETWORK_TYPE_NONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_notify.setOnClickListener {
            NotificationUtility.sendNotification(this)
        }

        btn_update.setOnClickListener {
            NotificationUtility.updateNotification(this)
        }

        btn_cancel.setOnClickListener {
            NotificationUtility.cancelNotification()
        }

        NotificationUtility.createNotificationChannel(this)
        setNotificationButtonState(true,false,false)
        registerReceiver(notifyReceiver, IntentFilter(NotificationUtility.ACTION_UPDATE_NOTIFICATION))
        registerReceiver(notifyReceiver, IntentFilter(ACTION_DISMISS_NOTIFICATION))

        btn_schedulejob.setOnClickListener {
            scheduleJob()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notifyReceiver)
    }



    // job scheduler is used to schedule a task when certain conditions are met
    fun scheduleJob(){
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        when(networkOptions.checkedRadioButtonId){
            R.id.anyNetwork->
                selectedNetwork = JobInfo.NETWORK_TYPE_UNMETERED
            R.id.noNetwork->
                selectedNetwork = JobInfo.NETWORK_TYPE_NONE
            R.id.wifiNetwork->
                selectedNetwork = JobInfo.NETWORK_TYPE_UNMETERED


        }

        //The ComponentName is used to associate the JobService with the JobInfo object.
        val serviceName = ComponentName(packageName,NotificationJobService::class.qualifiedName!!)
        val jobBuilder = JobInfo.Builder(JOB_ID,serviceName)
            .setRequiredNetworkType(selectedNetwork)
            .setRequiresDeviceIdle(deviceIdle.isChecked)
            .setRequiresCharging(deviceCharging.isChecked)

        val constrainstSet = selectedNetwork != JobInfo.NETWORK_TYPE_NONE
        if(constrainstSet|| deviceCharging.isChecked() || deviceIdle.isChecked()){
            val jobInfo = jobBuilder.build()
            jobScheduler.schedule(jobInfo)
            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Please set at least one constraint",
                Toast.LENGTH_SHORT).show();
        }


    }






    private fun setNotificationButtonState(notifyEnabled:Boolean,updateEnabled:Boolean,cancelEnabled:Boolean){
        btn_cancel.isEnabled = cancelEnabled
        btn_notify.isEnabled = notifyEnabled
        btn_update.isEnabled = updateEnabled

    }


    private inner class NotifyReceiver:BroadcastReceiver(){

        override fun onReceive(p0: Context?, intent: Intent?) {
            when(intent?.action){
                ACTION_UPDATE_NOTIFICATION->{
                    NotificationUtility.updateNotification(this@MainActivity)
                }
                ACTION_DISMISS_NOTIFICATION->{
                    setNotificationButtonState(true,true,true)
                }

            }

        }

    }

}
