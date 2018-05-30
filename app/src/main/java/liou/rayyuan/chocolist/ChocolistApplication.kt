package liou.rayyuan.chocolist

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class ChocolistApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }

}