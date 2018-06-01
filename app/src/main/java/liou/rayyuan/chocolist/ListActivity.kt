package liou.rayyuan.chocolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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
    private val videoFilterEditText: EditText by bindView(R.id.activity_list_search_edittext)
    private val videoFilterCancelImage: AppCompatImageView by bindView(R.id.activity_list_search_cancel_image)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        videoFilterEditText.isEnabled = false

        videoRepository = VideoRepository(
                (application as ChocolistApplication).apiManager,
                (application as ChocolistApplication).databaseManager,
                this
        )

        videoAdapter.videoItemClickListener = this
        videoFilterCancelImage.setOnClickListener {
            videoFilterEditText.setText("")
        }

        with(videoList) {
            setHasFixedSize(true)
            adapter = videoAdapter
        }

        fetchData()
    }

    // Text Change Listener
    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
        }

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            charSequence?.run {
                if (length > 0) {
                    if (isNotBlank()) {
                        videoRepository.findVideosByName(this.toString())
                        videoFilterCancelImage.visibility = View.VISIBLE
                    }

                } else {
                    videoFilterCancelImage.visibility = View.INVISIBLE
                    resetList()
                }
            }
        }
    }
    // End Text Change Listener

    override fun onResume() {
        super.onResume()
        videoFilterEditText.addTextChangedListener(textChangeListener)
    }

    override fun onPause() {
        super.onPause()
        videoFilterEditText.removeTextChangedListener(textChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoRepository.release()
    }

    override fun onBackPressed() {
        if (videoFilterEditText.text.isNotEmpty()) {
            videoFilterEditText.setText("")
        } else {
            super.onBackPressed()
        }
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
                if (viewState.videos.isNotEmpty()) {
                    videoAdapter.submitVideos(viewState.videos)
                    videoStateInfoText.visibility = View.GONE
                    videoList.visibility = View.VISIBLE
                } else {
                    videoStateInfoText.text = getString(R.string.search_no_result)
                    videoStateInfoText.visibility = View.VISIBLE
                    videoList.visibility = View.GONE
                }
                videoProgressBar.visibility = View.GONE
                videoFilterEditText.isEnabled = true
            }
            is ViewState.ErrorOccurred -> {
                videoStateInfoText.text = viewState.message
                videoProgressBar.visibility = View.GONE
                videoStateInfoText.visibility  = View.VISIBLE
                videoList.visibility = View.GONE
            }
        }
    }

    private fun resetList() {
        videoList.smoothScrollToPosition(0)
        videoAdapter.clean()
        videoRepository.prepareVideos()
    }

    //region VideoItemClickListener
    override fun onVideoItemClicked(video: Video) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(DetailActivity.targetDramaKey, video)
        intent.putExtras(bundle)
        startActivity(intent)
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
