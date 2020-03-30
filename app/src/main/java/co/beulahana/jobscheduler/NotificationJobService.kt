package co.beulahana.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import co.beulahana.NotificationUtility

/**
 * Created by Sandra Konotey on 2020-02-10.
 */
class NotificationJobService:JobService() {


    override fun onStartJob(p0: JobParameters?): Boolean {
        NotificationUtility.createNotificationChannel(this)
        NotificationUtility.sendNotification(this)

        return false //because for this app, all of the work is completed in the onStartJob() callback.
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true //because if the job fails, you want the job to be rescheduled instead of dropped.
    }
}