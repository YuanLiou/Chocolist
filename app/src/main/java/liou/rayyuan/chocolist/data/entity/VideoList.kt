package liou.rayyuan.chocolist.data.entity
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class VideoList(
    @SerializedName("data") val data: List<Video>
)

@Parcelize
data class Video(
        @SerializedName("drama_id") val dramaId: String,
        @SerializedName("name") val name: String,
        @SerializedName("total_views") val totalViews: Int,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("thumb") val thumb: String,
        @SerializedName("rating") val rating: Double
) : Parcelable