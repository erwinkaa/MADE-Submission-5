package id.erwinka.madesubmission4.main.movie

interface MovieView {
    fun showLoading()
    fun hideLoading()
    fun processMovieData(data: MovieResponseModel)
}