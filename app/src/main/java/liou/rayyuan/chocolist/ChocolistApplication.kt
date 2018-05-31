package liou.rayyuan.chocolist

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import liou.rayyuan.chocolist.data.APIManager
import liou.rayyuan.chocolist.data.DatabaseManager

class ChocolistApplication: Application() {

    val apiManager by lazy { APIManager() }
    val databaseManager by lazy { DatabaseManager.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }

}