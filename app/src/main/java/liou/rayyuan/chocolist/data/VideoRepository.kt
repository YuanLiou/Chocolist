package liou.rayyuan.chocolist.data

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.data.entity.VideoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoRepository(private val apiManager: APIManager, private val databaseManager: DatabaseManager,
                      private val listener: VideoRepositoryListener) {

    private val backgroundThread = HandlerThread("BackgroundThread").apply { start() }
    private var backgroundHandler: Handler? = Handler(backgroundThread.looper)
    private val mainHandler = Handler(Looper.getMainLooper())

    private var connection: Call<VideoList>? = null

    private var localVideoSize: Int = 0
    private var restoreFromCache = false

    fun prepareVideos() {
        val task = Runnable {
            localVideoSize = databaseManager.videoDao().getCacheCounts()

            mainHandler.post({
                if (localVideoSize > 0) {
                    queryVideoFromLocal()
                } else {
                    fetchVideos()
                }
            })
        }

        backgroundHandler?.post(task)
    }

    private fun queryVideoFromLocal() {
        val task = Runnable {
            val videos = databaseManager.videoDao().getAllCachedVideos()

            mainHandler.post({
                if (videos.isNotEmpty()) {
                    listener.onVideoFetched(videos)
                    Log.i("VideoRepository", "Video Restored From Local.")
                }
                restoreFromCache = true
            })
        }

        backgroundHandler?.post(task)
    }

    private fun fetchVideos() {
        Log.i("VideoRepository", "fetch video from internet")
        connection = apiManager.getVideoList()
        connection?.enqueue(object : Callback<VideoList> {
            override fun onResponse(call: Call<VideoList>?, response: Response<VideoList>?) {
                response?.run {
                    if (!isSuccessful) {
                        val errorString = errorBody()?.string() ?: "Response is not succeed."
                        listener.onVideoFetchError(errorString)
                        return
                    }
                    body()
                }?.apply {
                    if (data.isNotEmpty()) {
                        writeIntoLocal(data)
                    }
                }
            }

            override fun onFailure(call: Call<VideoList>?, throwable: Throwable?) {
                Log.e("ListActivity", Log.getStackTraceString(throwable))
                throwable?.run {
                    listener.onVideoFetchError(localizedMessage)
                }
            }
        })
    }

    private fun writeIntoLocal(videos: List<Video>) {
        val task = Runnable {
            for (video in videos) {
                databaseManager.videoDao().insertVideo(video)
            }

            Log.i("VideoRepository", "videos has been written into database")
            mainHandler.post({
                queryVideoFromLocal()
            })
        }

        backgroundHandler?.post(task)
    }

    fun release() {
        backgroundThread.quitSafely()
        backgroundThread.join()
        backgroundHandler = null
    }
}