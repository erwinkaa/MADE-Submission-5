package id.erwinka.madesubmission4.main.favorite.favoritetvshow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import id.erwinka.madesubmission4.main.tvshow.TVShowModel

class FavoriteTVShowViewModel : ViewModel() {
    private var listFavoriteMovies = MutableLiveData<List<TVShowModel>>()

    fun setMovies(data: List<TVShowModel>) {
        listFavoriteMovies.postValue(data)
    }

    fun getMovies() : LiveData<List<TVShowModel>> {
        return listFavoriteMovies
    }
}