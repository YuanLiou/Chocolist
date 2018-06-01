package liou.rayyuan.chocolist.utils

import android.content.Intent
import liou.rayyuan.chocolist.data.entity.Video

object DeepLinkHelper {

    fun targetVideo(intent: Intent): Video? {
        intent.data?.let { data ->
            // http://www.example.com/dramas/:id
            if (data.scheme == "http") {
                val path = data.pathSegments
                val dramaId = path.last()
                if (path.size == 2 && path[0] == "dramas") {
                    return Video().also {
                        it.dramaId = dramaId
                    }
                }
            }
        }
        return null
    }

    fun canHandle(intent: Intent): Boolean {
        if (intent.action != Intent.ACTION_VIEW) {
            return false
        }

        intent.data?.let { data ->
            val scheme = data.scheme
            // http://www.example.com/dramas/:id
            if (scheme == "http") {
                val path = data.pathSegments
                if (path.size == 2 && path[0] == "dramas") {
                    return true
                }
            }
        }
        return false
    }

}