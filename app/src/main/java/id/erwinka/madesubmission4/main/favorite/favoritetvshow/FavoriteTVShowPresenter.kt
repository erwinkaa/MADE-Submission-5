package id.erwinka.madesubmission4.main.favorite.favoritetvshow

import android.content.Context
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.main.tvshow.TVShowModel
import id.erwinka.madesubmission4.sqllite.FavoriteDatabase
import id.erwinka.madesubmission4.sqllite.database
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteTVShowPresenter(private val view: FavoriteTVShowView) {

    private var data = mutableListOf<TVShowModel>()

    fun getFavoriteTVShow(ctx: Context?) {
        view.showLoading()

        ctx?.database?.use {
            data.clear()
            val result = select(FavoriteDatabase.TABLE_FAVORITE)
                .whereArgs("${FavoriteDatabase.FILM_TYPE} = '${MainActivity.TV}'")
            val favorite = result.parseList(classParser<FavoriteDatabase>())
            for (i in 0 until favorite.size) {
                data.add(
                    TVShowModel(
                        favorite[i].filmId,
                        favorite[i].filmTitle,
                        favorite[i].posterPath
                    )
                )
            }
        }

        view.hideLoading()
        view.processFavTVShowData(data)
    }
}