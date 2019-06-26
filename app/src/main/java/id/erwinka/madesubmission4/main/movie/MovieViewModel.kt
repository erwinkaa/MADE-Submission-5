package id.erwinka.madesubmission4.main.movie

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MovieViewModel : ViewModel() {

    private var listMovies = MutableLiveData<MovieResponseModel>()

    fun setMovies(data: MovieResponseModel) {
        listMovies.postValue(data)
    }

    fun getMovies() : LiveData<MovieResponseModel> {
        return listMovies
    }

}