package liou.rayyuan.chocolist

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import liou.rayyuan.chocolist.adapter.VideoListAdapter
import liou.rayyuan.chocolist.data.APIManager
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.data.entity.VideoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {
    private val apiManager = APIManager()
    private val videoAdapter = VideoListAdapter()

    private sealed class ViewState {
        class Success(val videos: List<Video>): ViewState()
        class ErrorOccurred(val message: String): ViewState()
        object Loading: ViewState()
    }

    private val videoList: RecyclerView by bindView(R.id.activity_list_videos)
    private val videoProgressBar: ProgressBar by bindView(R.id.activity_list_progress_bar)
    private val videoStateInfoText: TextView by bindView(R.id.activity_list_state_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        with(videoList) {
            setHasFixedSize(true)
            adapter = videoAdapter
        }

        fetchData()
    }

    private fun fetchData() {
        setVideoListState(ViewState.Loading)
        // fetch data
        apiManager.getVideoList().enqueue(object : Callback<VideoList> {
            override fun onResponse(call: Call<VideoList>?, response: Response<VideoList>?) {
                response?.run {
                    if (!isSuccessful) {
                        setVideoListState(ViewState.ErrorOccurred("Response is not succeed."))
                        return
                    }
                    body()
                }?.apply {
                    if (data.isNotEmpty()) {
                        setVideoListState(ViewState.Success(data))
                    }
                }
            }

            override fun onFailure(call: Call<VideoList>?, throwable: Throwable?) {
                Log.e("ListActivity", Log.getStackTraceString(throwable))
                throwable?.run {
                    setVideoListState(ViewState.ErrorOccurred(localizedMessage))
                }
            }
        })
    }

    private fun setVideoListState(viewState: ViewState) {
        when (viewState) {
            ViewState.Loading -> {
                videoProgressBar.visibility = View.VISIBLE
                videoStateInfoText.visibility = View.GONE
                videoList.visibility = View.GONE
            }
            is ViewState.Success -> {
                videoAdapter.addVideos(viewState.videos)
                videoProgressBar.visibility = View.GONE
                videoStateInfoText.visibility  = View.GONE
                videoList.visibility = View.VISIBLE
            }
            is ViewState.ErrorOccurred -> {
                videoStateInfoText.text = viewState.message
                videoProgressBar.visibility = View.GONE
                videoStateInfoText.visibility  = View.VISIBLE
                videoList.visibility = View.GONE
            }
        }
    }

    private fun <T: View> Activity.bindView(@IdRes resId: Int): Lazy<T> = lazy {
        findViewById<T>(resId)
    }
}
