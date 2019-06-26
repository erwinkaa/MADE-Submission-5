package id.erwinka.madesubmission4.main.favorite.favoritemovie

import android.content.Context
import id.erwinka.madesubmission4.main.MainActivity
import id.erwinka.madesubmission4.main.movie.MovieModel
import id.erwinka.madesubmission4.sqllite.FavoriteDatabase
import id.erwinka.madesubmission4.sqllite.database
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteMoviePresenter(private val view: FavoriteMovieView) {

    private var data = mutableListOf<MovieModel>()

    fun getFavoriteMovie(ctx: Context?) {
        view.showLoading()

        ctx?.database?.use {
            data.clear()
            val result = select(FavoriteDatabase.TABLE_FAVORITE)
                .whereArgs("${FavoriteDatabase.FILM_TYPE} = '${MainActivity.MOVIE}'")
            val favorite = result.parseList(classParser<FavoriteDatabase>())
            for (i in 0 until favorite.size) {
                data.add(MovieModel(
                    favorite[i].filmId,
                    favorite[i].filmTitle,
                    favorite[i].posterPath
                ))
            }
        }

        view.hideLoading()
        view.processFavMovieData(data)
    }
}