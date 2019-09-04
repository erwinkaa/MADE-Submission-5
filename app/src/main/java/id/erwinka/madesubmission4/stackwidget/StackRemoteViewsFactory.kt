package id.erwinka.madesubmission4.stackwidget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.sqllite.FavoriteDatabase
import id.erwinka.madesubmission4.sqllite.database
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import java.lang.Exception
import java.util.concurrent.ExecutionException

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val data = mutableListOf<FavoriteFilmModel>()

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? { return null }

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() { loadData() }

    override fun hasStableIds(): Boolean { return false }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        try {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(ApiRepository.BASE_IMAGE_URL + data[position].posterPath)
                .apply(RequestOptions().centerCrop())
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.iv_widget, bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val bundle = Bundle()
        bundle.putString(FavoriteWidget.FILM_ID, data[position].id)
        bundle.putString(FavoriteWidget.FILM_TYPE, data[position].type)
        val intent = Intent()
        intent.putExtras(bundle)

        rv.setOnClickFillInIntent(R.id.iv_widget, intent)
        return rv
    }

    override fun getCount(): Int = data.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

    private fun loadData() {
        context.database.use {
            data.clear()
            val result = select(FavoriteDatabase.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteDatabase>())
            for (i in 0 until favorite.size) {
                data.add(
                    FavoriteFilmModel(
                        favorite[i].filmId,
                        favorite[i].posterPath,
                        favorite[i].filmType
                    )
                )
            }
        }
    }

    private data class FavoriteFilmModel(
        val id: String,
        val posterPath: String,
        val type: String
    )

}