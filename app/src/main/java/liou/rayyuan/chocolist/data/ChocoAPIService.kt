package liou.rayyuan.chocolist.data

import liou.rayyuan.chocolist.data.entity.VideoList
import retrofit2.Call
import retrofit2.http.GET

interface ChocoAPIService {

    @GET("5a97c59c30000047005c1ed2")
    fun getVideoList(): Call<VideoList>

}