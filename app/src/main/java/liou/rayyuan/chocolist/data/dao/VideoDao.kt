package liou.rayyuan.chocolist.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import liou.rayyuan.chocolist.data.entity.Video

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    fun getAllCachedVideos(): List<Video>

    @Query("SELECT count(*) FROM videos ")
    fun getCacheCounts(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo(vararg video: Video)

    @Query("DELETE FROM videos ")
    fun deleteAllVideos()

    @Query("""
        SELECT * FROM videos
        WHERE name LIKE '%' || :keyword || '%'
    """)
    fun findVideosContainsWords(keyword: String): List<Video>

}