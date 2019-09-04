package id.erwinka.madesubmission4.setting

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.main.movie.MovieResponseModel
import id.erwinka.madesubmission4.main.detail.DetailActivity
import id.erwinka.madesubmission4.util.LOG_TAG
import id.erwinka.madesubmission4.util.getLocale
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    private val intentKey = "KEY"

    companion object {
        const val DAILY = 100
        const val RELEASE_DATE = 101
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.extras?.getInt(intentKey) == DAILY) {
            sendDailyNotification(context)
        } else if (intent?.extras?.getInt(intentKey) == RELEASE_DATE) {
            loadMovies(context)
        }
    }

    fun setRepeatingAlarm(context: Context?, type: Int) {

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (type == DAILY) {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(intentKey, DAILY)
            val pendingIntent = PendingIntent.getBroadcast(context, DAILY, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent
            )
            context.toast(context.resources.getString(R.string.daily_subsribed))

        } else if (type == RELEASE_DATE) {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(intentKey, RELEASE_DATE)
            val pendingIntent = PendingIntent.getBroadcast(context, RELEASE_DATE, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent
            )
            context.toast(context.resources.getString(R.string.release_today_subsribed))
        }
    }

    fun cancelRepeatingAlarm(context: Context?, type: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, type, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        if (type == DAILY) {
            context.toast(context.resources.getString(R.string.daily_unsubsribed))
        } else if (type == RELEASE_DATE) {
            context.toast(context.resources.getString(R.string.release_today_unsubsribed))
        }
    }

    private fun loadMovies(context: Context) {
        val service = ApiRepository.create()
        service.loadMovie(ApiRepository.API_KEY, getLocale()).enqueue(object : Callback<MovieResponseModel> {
            override fun onResponse(call: Call<MovieResponseModel>, response: Response<MovieResponseModel>) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                    processNotification(context, data)
                }
            }

            override fun onFailure(call: Call<MovieResponseModel>, error: Throwable) {
                Log.e(LOG_TAG, "${error.message}")
            }
        })
    }

    private fun processNotification(context: Context, data: MovieResponseModel) {
        var notificationId = 0
        for (i in notificationId until data.results.size) {
            if (checkDate(data.results[i].release_date)) {
                sendReleaseTodayNotification(
                    context,
                    notificationId,
                    data.results[i].id,
                    data.results[i].title,
                    data.results[i].overview
                )
                notificationId++
            }
        }
    }

    private fun checkDate(releaseDate: String): Boolean {
        val pattern = "yyyy-MM-dd"
        val parsedReleaseDate = SimpleDateFormat(pattern, Locale.getDefault()).parse(releaseDate)
        val dateNow = SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
        val parsedDateNow = SimpleDateFormat(pattern, Locale.getDefault()).parse(dateNow)
        return parsedReleaseDate.compareTo(parsedDateNow) == 0 //really released today
//        return parsedReleaseDate < parsedDateNow //for debugging
    }

    private fun sendDailyNotification(context: Context) {
        val channelId = context.resources.getString(R.string.daily_notification_channel_id)
        val channelName = context.resources.getString(R.string.daily_notification_channel_name)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val builder: NotificationCompat.Builder
        builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(context.resources.getString(R.string.daily_notif_title))
            .setContentText(context.resources.getString(R.string.daily_notif_content))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(999, notification)
    }

    private fun sendReleaseTodayNotification(
        context: Context,
        notificationId: Int,
        filmId: String,
        title: String,
        content: String
    ) {
        val channelId = context.resources.getString(R.string.release_today_notification_channel_id)
        val channelName = context.resources.getString(R.string.release_today_notification_channel_name)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(MainActivity.DATA_EXTRA, filmId)
        intent.putExtra(MainActivity.TYPE, MainActivity.MOVIE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val builder: NotificationCompat.Builder
        builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(context.resources.getString(R.string.nowplaying) + title)
            .setContentText(content)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }

}