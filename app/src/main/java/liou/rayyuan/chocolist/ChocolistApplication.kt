package liou.rayyuan.chocolist

import android.app.Application
import android.graphics.Bitmap
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import liou.rayyuan.chocolist.data.APIManager
import liou.rayyuan.chocolist.data.DatabaseManager

class ChocolistApplication: Application() {

    val apiManager by lazy { APIManager() }
    val databaseManager by lazy { DatabaseManager.getInstance(this) }

    override fun onCreate() {
        super.onCreate()

        val pipelineBuilder = OkHttpImagePipelineConfigFactory.newBuilder(this, apiManager.httpClient)
        with(pipelineBuilder) {
            isDownsampleEnabled = true
            setResizeAndRotateEnabledForNetwork(true)
            setBitmapsConfig(Bitmap.Config.RGB_565)
        }

        Fresco.initialize(this, pipelineBuilder.build())
    }

}