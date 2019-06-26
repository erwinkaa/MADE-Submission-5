package id.erwinka.madesubmission4.main.favorite.favoritetvshow

import id.erwinka.madesubmission4.main.tvshow.TVShowModel

interface FavoriteTVShowView {
    fun showLoading()
    fun hideLoading()
    fun processFavTVShowData(data: List<TVShowModel>)
}