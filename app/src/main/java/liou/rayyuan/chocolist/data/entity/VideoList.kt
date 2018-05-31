package liou.rayyuan.chocolist.data.entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import liou.rayyuan.chocolist.data.DateTypeConverter
import java.util.*

data class VideoList(
    @SerializedName("data") val data: List<Video>
)

@Entity(tableName = "videos")
@TypeConverters(DateTypeConverter::class)
@Parcelize
data class Video(
        @PrimaryKey @ColumnInfo(name = "drama_id") @SerializedName("drama_id") var dramaId: String,
        @SerializedName("name") var name: String?,
        @ColumnInfo(name = "total_views") @SerializedName("total_views") var totalViews: Int?,
        @ColumnInfo(name = "created_at") @SerializedName("created_at") var createdAt: Date?,
        @SerializedName("thumb") var thumb: String?,
        @SerializedName("rating") var rating: Double?
) : Parcelable {
    // DIRTY HACK:: if Room, Parcelize and kapt needs an empty constructor. Make that for it.
    constructor(): this("", "", 0, Date(), "", 0.0)
}