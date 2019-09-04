package id.erwinka.madesubmission4.main.detail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.api.ApiService
import id.erwinka.madesubmission4.sqllite.FavoriteDatabase
import id.erwinka.madesubmission4.sqllite.database
import id.erwinka.madesubmission4.util.LOG_TAG
import id.erwinka.madesubmission4.util.getLocale
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.ComponentName
import android.app.job.JobScheduler
import android.os.Build
import android.app.job.JobInfo
import id.erwinka.madesubmission4.stackwidget.UpdateWidgetService
import id.erwinka.madesubmission4.util.random


class DetailFilmPresenter(
    private val view: DetailFilmView,
    private val apiService: ApiService
) {
    fun loadMovieDetail(id: String) {
        view.showLoading()
        apiService.loadMovieDetail(id, ApiRepository.API_KEY, getLocale()).enqueue(object : Callback<DetailMovieModel> {
            override fun onResponse(call: Call<DetailMovieModel>, response: Response<DetailMovieModel>) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                    view.processMovieData(data)
                }
                view.hideLoading()
            }

            override fun onFailure(call: Call<DetailMovieModel>, error: Throwable) {
                Log.e(LOG_TAG, "${error.message}")
                view.hideLoading()
            }
        })
    }

    fun loadTVShowDetail(id: String) {
        view.showLoading()
        apiService.loadTVShowDetail(id, ApiRepository.API_KEY, getLocale())
            .enqueue(object : Callback<DetailTVShowModel> {
                override fun onResponse(call: Call<DetailTVShowModel>, response: Response<DetailTVShowModel>) {
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        view.processTVShowData(data)
                    }
                    view.hideLoading()
                }

                override fun onFailure(call: Call<DetailTVShowModel>, error: Throwable) {
                    Log.e(LOG_TAG, "${error.message}")
                    view.hideLoading()
                }
            })
    }

    fun addToFavorite(
        ctx: Context,
        filmId: String,
        filmType: String,
        filmTitle: String,
        posterPath: String,
        overview: String,
        releaseDate: String
    ) {
        try {
            ctx.database.use {
                insert(
                    FavoriteDatabase.TABLE_FAVORITE,
                    FavoriteDatabase.FILM_ID to filmId,
                    FavoriteDatabase.FILM_TYPE to filmType,
                    FavoriteDatabase.FILM_TITLE to filmTitle,
                    FavoriteDatabase.POSTER_PATH to posterPath,
                    FavoriteDatabase.OVERVIEW to overview,
                    FavoriteDatabase.RELEASE_DATE to releaseDate
                )
            }
            ctx.toast(R.string.TOAST_FAVORITE).show()
            updateWidget(ctx)
        } catch (e: SQLiteConstraintException) {
            ctx.toast(e.localizedMessage).show()
        }
    }

    fun removeFromFavorite(ctx: Context, filmId: String) {
        try {
            ctx.database.use {
                delete(
                    FavoriteDatabase.TABLE_FAVORITE,
                    "(FILM_ID = {id})",
                    "id" to filmId
                )
            }
            ctx.toast(R.string.TOAST_UNFAVORITE).show()
            updateWidget(ctx)
        } catch (e: SQLiteConstraintException) {
            ctx.toast(e.localizedMessage).show()
        }
    }

    fun favoriteState(ctx: Context, filmId: String): Boolean {
        var isFavorited = false

        ctx.database.use {
            val result = select(FavoriteDatabase.TABLE_FAVORITE)
                .whereArgs(
                    "(FILM_ID = {id})",
                    "id" to filmId
                )
            val favorite = result.parseList(classParser<FavoriteDatabase>())
            if (favorite.isNotEmpty()) {
                isFavorited = true
            }
        }
        return isFavorited
    }

    //TODO: WHY IT DOESN'T WORKS?! changed to click to refresh
    private fun updateWidget(context: Context) {
        val jobId = (0 until 200).random()
        val millis: Long = 10000
        val serviceComponent = ComponentName(context, UpdateWidgetService::class.java)
        val builder = JobInfo.Builder(jobId, serviceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(millis)
        } else {
            builder.setPeriodic(millis)
        }
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

}