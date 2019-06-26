package id.erwinka.madesubmission4.main.tvshow

interface TVShowView {
    fun showLoading()
    fun hideLoading()
    fun processTVShowData(data: TVShowResponseModel)
}