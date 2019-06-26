package id.erwinka.madesubmission4.main.movie

import android.util.Log
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.api.ApiService
import id.erwinka.madesubmission4.util.LOG_TAG
import id.erwinka.madesubmission4.util.getLocale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviePresenter(
    private val view: MovieView,
    private val apiService: ApiService
) {
    fun loadMovie() {
        view.showLoading()
        apiService.loadMovie(ApiRepository.API_KEY, getLocale()).enqueue(object : Callback<MovieResponseModel> {
            override fun onResponse(call: Call<MovieResponseModel>, response: Response<MovieResponseModel>) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                    view.processMovieData(data)
                }
                view.hideLoading()
            }

            override fun onFailure(call: Call<MovieResponseModel>, error: Throwable) {
                Log.e(LOG_TAG, "${error.message}")
                view.hideLoading()
            }
        })
    }
}