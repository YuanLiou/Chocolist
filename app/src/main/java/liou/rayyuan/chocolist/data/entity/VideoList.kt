package liou.rayyuan.chocolist.data.entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class VideoList(
    @SerializedName("data") val data: List<Video>
)

@Entity(tableName = "videos")
@Parcelize
data class Video(
        @PrimaryKey @ColumnInfo(name = "drama_id") @SerializedName("drama_id") val dramaId: String,
        @SerializedName("name") val name: String,
        @ColumnInfo(name = "total_views") @SerializedName("total_views") val totalViews: Int,
        @Ignore @SerializedName("created_at") val createdAt: Date,
        @SerializedName("thumb") val thumb: String,
        @SerializedName("rating") val rating: Double
) : Parcelable