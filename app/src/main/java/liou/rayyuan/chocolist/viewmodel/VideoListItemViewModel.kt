package liou.rayyuan.chocolist.viewmodel

import android.content.Context
import liou.rayyuan.chocolist.R
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.utils.DateUtils

class VideoListItemViewModel(private val video: Video) {

    fun getPublishDate(context: Context): String {
        val formattedDate = DateUtils.getFormattedDate(video.createdAt)
        return context.getString(R.string.title_created_at, formattedDate)
    }

    fun getRatingScore(context: Context): String {
        val rating = String.format("%.1f", video.rating)
        return context.getString(R.string.title_rating, rating)
    }

}