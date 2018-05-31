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
import liou.rayyuan.chocolist.adapter.VideoItemClickListener
import liou.rayyuan.chocolist.adapter.VideoListAdapter
import liou.rayyuan.chocolist.data.VideoRepository
import liou.rayyuan.chocolist.data.VideoRepositoryListener
import liou.rayyuan.chocolist.data.entity.Video

class ListActivity : AppCompatActivity(), VideoItemClickListener, VideoRepositoryListener {
    private val videoAdapter = VideoListAdapter()
    private lateinit var videoRepository: VideoRepository

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

        videoRepository = VideoRepository(
                (application as ChocolistApplication).apiManager,
                (application as ChocolistApplication).databaseManager,
                this
        )

        videoAdapter.videoItemClickListener = this
        with(videoList) {
            setHasFixedSize(true)
            adapter = videoAdapter
        }

        fetchData()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoRepository.release()
    }

    private fun fetchData() {
        setVideoListState(ViewState.Loading)
        // fetch data
        videoRepository.prepareVideos()
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

    //region VideoItemClickListener
    override fun onVideoItemClicked(video: Video) {
        Log.i("ListActivity", "Video item clicked: ${video.name}")
    }
    //endregion

    //region VideoRepositoryListener
    override fun onVideoFetched(videos: List<Video>) {
        setVideoListState(ViewState.Success(videos))
    }

    override fun onVideoFetchError(message: String) {
        setVideoListState(ViewState.ErrorOccurred(message))
    }
    //endregion

    private fun <T: View> Activity.bindView(@IdRes resId: Int): Lazy<T> = lazy {
        findViewById<T>(resId)
    }
}
