package liou.rayyuan.chocolist.data

import com.google.gson.GsonBuilder
import liou.rayyuan.chocolist.BuildConfig
import liou.rayyuan.chocolist.data.entity.VideoList
import liou.rayyuan.chocolist.utils.DateUtils
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

    val httpClient: OkHttpClient

    init {
        val logInterceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        httpClient = OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

        val gson = GsonBuilder()
                .setDateFormat(DateUtils.datePattern)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL + apiVersion)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()

        chocoAPIService = retrofit.create(ChocoAPIService::class.java)
    }

    fun getVideoList(): Call<VideoList> {
        return chocoAPIService.getVideoList()
    }
}