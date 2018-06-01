package liou.rayyuan.chocolist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.utils.DeepLinkHelper

class DetailActivity: AppCompatActivity() {
    companion object {
        const val targetDramaKey = "target-drama-key"
    }

    private var drama: Video? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent?.let {
            if (DeepLinkHelper.canHandle(it)) {
                // is Deep link
                val partialVideoData = DeepLinkHelper.targetVideo(it)
                partialVideoData?.let {
                    printData(it)
                }
            } else {
                // is Normal Intent
                it.extras?.run {
                    if (containsKey(targetDramaKey)) {
                        drama = getParcelable(targetDramaKey)

                        drama?.let {
                            printData(it)
                        }
                    }
                }
            }
        }
    }

    private fun printData(video: Video) {
        detail_test_textView.text = """
            drama id = ${video.dramaId}, name = ${video.name ?: ""}, create at = ${video.createdAt ?: ""} \n
            rating = ${video.rating ?: 0.0f}
            """
    }
}