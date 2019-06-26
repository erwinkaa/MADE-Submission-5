package id.erwinka.madesubmission4.main.tvshow

import android.util.Log
import id.erwinka.madesubmission4.api.ApiRepository
import id.erwinka.madesubmission4.api.ApiService
import id.erwinka.madesubmission4.util.LOG_TAG
import id.erwinka.madesubmission4.util.getLocale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowPresenter (
    private val view: TVShowView,
    private val apiService: ApiService
) {
    fun loadTVShow() {
        view.showLoading()
        apiService.loadTVShow(ApiRepository.API_KEY, getLocale()).enqueue(object : Callback<TVShowResponseModel> {
            override fun onResponse(call: Call<TVShowResponseModel>, response: Response<TVShowResponseModel>) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                    view.processTVShowData(data)
                }
                view.hideLoading()
            }

            override fun onFailure(call: Call<TVShowResponseModel>, error: Throwable) {
                Log.e(LOG_TAG, "${error.message}")
                view.hideLoading()
            }
        })
    }
}