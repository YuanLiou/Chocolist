package liou.rayyuan.chocolist

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.Group
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.utils.DeepLinkHelper
import liou.rayyuan.chocolist.viewmodel.VideoItemViewModel

class DetailActivity: AppCompatActivity() {
    companion object {
        const val targetDramaKey = "target-drama-key"
    }

    private sealed class ViewState {
        class ReadyToShow(val video: Video): ViewState()
        class ErrorOccurred(val message: String): ViewState()
    }

    private val mainUIGroup: Group by bindView(R.id.activity_detail_main_content_group)
    private val errorUIGroup: Group by bindView(R.id.activity_detail_error_group)

    // main UI group
    private val dramaThumb: SimpleDraweeView by bindView(R.id.activity_detail_thumb)
    private val coverView: SimpleDraweeView by bindView(R.id.activity_detail_thumb_blur)
    private val titleText: TextView by bindView(R.id.activity_detail_title)
    private val totalViewText: TextView by bindView(R.id.activity_detail_total_view)
    private val createAtTimeText: TextView by bindView(R.id.activity_detail_create_at)
    private val ratingText: TextView by bindView(R.id.activity_detail_rating)

    // error UI Group
    private val errorText: TextView by bindView(R.id.activity_detail_status_text)
    private val errorButton: Button by bindView(R.id.activity_detail_retry_button)

    private var drama: Video? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent?.let {
            if (DeepLinkHelper.canHandle(it)) {
                // is Deep link
                val partialVideoData = DeepLinkHelper.targetVideo(it)
                partialVideoData?.let {
                    // Should Fetch Detailed Info
//                    setDetailPageState(ViewState.ReadyToShow(it))
                }
            } else {
                // is Normal Intent
                it.extras?.run {
                    if (containsKey(targetDramaKey)) {
                        drama = getParcelable(targetDramaKey)

                        drama?.let {
                            setDetailPageState(ViewState.ReadyToShow(it))
                        }
                    }
                }
            }
        }
    }

    private fun setDetailPageState(viewState: ViewState) {
        when (viewState) {
            is ViewState.ReadyToShow -> {
                mainUIGroup.visibility = View.VISIBLE
                errorUIGroup.visibility = View.GONE

                val videoViewModel = VideoItemViewModel(viewState.video)
                titleText.text = viewState.video.name
                createAtTimeText.text = videoViewModel.getPublishDate(this)
                ratingText.text = videoViewModel.getRatingScore(this)
                totalViewText.text = videoViewModel.getTotalViewCounts(this)

                viewState.video.thumb?.run {
                    dramaThumb.setImageURI(this)
                    coverView.setBlurryImage(this, 20)
                }
            }
            is ViewState.ErrorOccurred -> {
                errorUIGroup.visibility = View.VISIBLE
                mainUIGroup.visibility = View.GONE
            }
        }
    }

    private fun SimpleDraweeView.setBlurryImage(url: String, blurRadious: Int) {
        val uri = Uri.parse(url)
        val imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(IterativeBoxBlurPostProcessor(blurRadious))
                .build()
        val imageController = Fresco.newDraweeControllerBuilder()
                .setOldController(controller)
                .setImageRequest(imageRequest)
                .build()
        controller = imageController
    }

    private fun <T: View> Activity.bindView(@IdRes resId: Int): Lazy<T> = lazy {
        findViewById<T>(resId)
    }
}