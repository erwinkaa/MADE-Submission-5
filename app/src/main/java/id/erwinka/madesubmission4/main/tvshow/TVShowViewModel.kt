package id.erwinka.madesubmission4.main.tvshow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class TVShowViewModel : ViewModel() {

    private var listTVShows = MutableLiveData<TVShowResponseModel>()

    fun setTVShows(data: TVShowResponseModel) {
        listTVShows.postValue(data)
    }

    fun getTVShows() : LiveData<TVShowResponseModel> {
        return listTVShows
    }

}