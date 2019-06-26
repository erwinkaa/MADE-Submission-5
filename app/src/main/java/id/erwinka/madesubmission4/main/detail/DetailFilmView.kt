package id.erwinka.madesubmission4.main.detail

interface DetailFilmView {
    fun showLoading()
    fun hideLoading()
    fun processMovieData(data: DetailMovieModel)
    fun processTVShowData(data: DetailTVShowModel)
}