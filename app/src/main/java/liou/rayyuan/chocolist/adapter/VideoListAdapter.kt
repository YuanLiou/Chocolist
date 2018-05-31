package liou.rayyuan.chocolist.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import liou.rayyuan.chocolist.R
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.viewmodel.VideoListItemViewModel

class VideoListAdapter: ListAdapter<Video, VideoListAdapter.VideoItemViewHolder>(videoDiffCallback) {

    companion object {
        private val videoDiffCallback = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video?, newItem: Video?): Boolean {
                return oldItem?.dramaId == newItem?.dramaId
            }

            override fun areContentsTheSame(oldItem: Video?, newItem: Video?): Boolean {
                return oldItem == newItem
            }
        }
    }

    var videoItemClickListener: VideoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_list_item, parent, false)
        return VideoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        if (itemCount <= 0) {
            return
        }

        val index = holder.adapterPosition
        val video = getItem(index)
        val viewModel = VideoListItemViewModel(video)
        holder.videoThumb.setImageURI(video.thumb)
        holder.videoTitle.text = video.name
        holder.videoCreateTime.text = viewModel.getPublishDate(holder.videoCreateTime.context)
        holder.videoRating.text = viewModel.getRatingScore(holder.videoRating.context)
    }

    fun submitVideos(videos: List<Video>) {
        submitList(videos)
    }

    fun clean() {
        submitList(emptyList())
    }

    inner class VideoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val videoThumb: SimpleDraweeView = itemView.findViewById(R.id.video_item_thumb)
        internal val videoTitle: TextView = itemView.findViewById(R.id.video_item_title)
        internal val videoCreateTime: TextView = itemView.findViewById(R.id.video_item_created_at_time)
        internal val videoRating: TextView = itemView.findViewById(R.id.video_item_rating)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    videoItemClickListener?.onVideoItemClicked(getItem(adapterPosition))
                }
            }
        }
    }
}