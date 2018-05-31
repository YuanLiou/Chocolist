package liou.rayyuan.chocolist.data

import liou.rayyuan.chocolist.data.entity.Video

interface VideoRepositoryListener {
    fun onVideoFetched(videos: List<Video>)
    fun onVideoFetchError(message: String)
}