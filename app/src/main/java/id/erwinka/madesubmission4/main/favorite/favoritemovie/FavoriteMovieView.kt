package id.erwinka.madesubmission4.main.favorite.favoritemovie

import id.erwinka.madesubmission4.main.movie.MovieModel

interface FavoriteMovieView {
    fun showLoading()
    fun hideLoading()
    fun processFavMovieData(data: List<MovieModel>)
}