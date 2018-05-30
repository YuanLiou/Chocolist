package liou.rayyuan.chocolist.data

import liou.rayyuan.chocolist.BuildConfig
import liou.rayyuan.chocolist.data.entity.VideoList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIManager {
    private val chocoAPIService: ChocoAPIService
    private val baseURL = "http://www.mocky.io/"
    private val apiVersion = "v2/"

    init {
        val logInterceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL + apiVersion)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

        chocoAPIService = retrofit.create(ChocoAPIService::class.java)
    }

    fun getVideoList(): Call<VideoList> {
        return chocoAPIService.getVideoList()
    }
}