package liou.rayyuan.chocolist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import liou.rayyuan.chocolist.R
import liou.rayyuan.chocolist.data.entity.Video
import liou.rayyuan.chocolist.viewmodel.VideoListItemViewModel

class VideoListAdapter: RecyclerView.Adapter<VideoListAdapter.VideoItemViewHolder>() {
    var videoItemClickListener: VideoItemClickListener? = null
    private val videos = mutableListOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_list_item, parent, false)
        return VideoItemViewHolder(view)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        if (videos.isEmpty()) {
            return
        }

        val index = holder.adapterPosition
        val video = videos[index]
        val viewModel = VideoListItemViewModel(video)
        holder.videoThumb.setImageURI(video.thumb)
        holder.videoTitle.text = video.name
        holder.videoCreateTime.text = viewModel.getPublishDate(holder.videoCreateTime.context)
        holder.videoRating.text = viewModel.getRatingScore(holder.videoRating.context)
    }

    fun addVideos(videos: List<Video>) {
        val originalIndex = this.videos.size
        this.videos.addAll(videos)
        notifyItemRangeInserted(originalIndex, videos.size)
    }

    fun clean() {
        this.videos.clear()
        notifyDataSetChanged()
    }

    inner class VideoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val videoThumb: SimpleDraweeView = itemView.findViewById(R.id.video_item_thumb)
        internal val videoTitle: TextView = itemView.findViewById(R.id.video_item_title)
        internal val videoCreateTime: TextView = itemView.findViewById(R.id.video_item_created_at_time)
        internal val videoRating: TextView = itemView.findViewById(R.id.video_item_rating)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    videoItemClickListener?.onVideoItemClicked(videos[adapterPosition])
                }
            }
        }
    }
}