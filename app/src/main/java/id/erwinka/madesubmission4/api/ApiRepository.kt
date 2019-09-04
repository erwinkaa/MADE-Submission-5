package id.erwinka.madesubmission4.api

import id.erwinka.madesubmission4.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRepository {

    private var BASE_URL: String = BuildConfig.BASE_URL
    var API_KEY: String = BuildConfig.TSDB_API_KEY
    var BASE_IMAGE_URL: String = BuildConfig.IMAGE_BASE_URL

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}