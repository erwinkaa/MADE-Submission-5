package id.erwinka.madesubmission4.api

import id.erwinka.madesubmission4.main.detail.DetailMovieModel
import id.erwinka.madesubmission4.main.detail.DetailTVShowModel
import id.erwinka.madesubmission4.main.movie.MovieResponseModel
import id.erwinka.madesubmission4.main.tvshow.TVShowResponseModel
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    fun loadMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<MovieResponseModel>

    @GET("discover/tv")
    fun loadTVShow(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<TVShowResponseModel>

    @GET("movie/{id}")
    fun loadMovieDetail(
        @Path("id") id: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<DetailMovieModel>

    @GET("tv/{id}")
    fun loadTVShowDetail(
        @Path("id") id: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US"
    ): Call<DetailTVShowModel>

    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US",
        @Query("query") query: String
    ): Call<MovieResponseModel>

    @GET("search/tv")
    fun searchTVShow(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "en-US",
        @Query("query") query: String
    ): Call<TVShowResponseModel>

}