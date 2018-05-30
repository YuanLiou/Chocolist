package liou.rayyuan.chocolist.adapter

import liou.rayyuan.chocolist.data.entity.Video

interface VideoItemClickListener {
    fun onVideoItemClicked(video: Video)
}