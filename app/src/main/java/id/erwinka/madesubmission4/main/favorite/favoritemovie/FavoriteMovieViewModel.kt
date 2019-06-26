package id.erwinka.madesubmission4.main.favorite.favoritemovie

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import id.erwinka.madesubmission4.main.movie.MovieModel

class FavoriteMovieViewModel : ViewModel() {
    private var listFavoriteMovies = MutableLiveData<List<MovieModel>>()

    fun setMovies(data: List<MovieModel>) {
        listFavoriteMovies.postValue(data)
    }

    fun getMovies() : LiveData<List<MovieModel>> {
        return listFavoriteMovies
    }
}