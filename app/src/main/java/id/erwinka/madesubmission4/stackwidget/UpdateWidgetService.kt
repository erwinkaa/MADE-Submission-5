package id.erwinka.madesubmission4.stackwidget

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.RemoteViews
import id.erwinka.madesubmission4.R

class UpdateWidgetService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean { return false }

    override fun onStartJob(params: JobParameters?): Boolean {
        val manager = AppWidgetManager.getInstance(this)
        val view = RemoteViews(packageName, R.layout.favorite_widget)
        val theWidget = ComponentName(this, FavoriteWidget::class.java)
        manager.updateAppWidget(theWidget, view)
        jobFinished(params, false)
        return true
    }
}