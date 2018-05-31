package liou.rayyuan.chocolist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import liou.rayyuan.chocolist.data.dao.VideoDao
import liou.rayyuan.chocolist.data.entity.Video

@Database(entities = [(Video::class)], version = 1)
abstract class DatabaseManager: RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {
        const val databaseName = "chocolist_ab"
        private var instance: DatabaseManager? = null

        fun getInstance(context: Context): DatabaseManager? {
            if (instance == null) {
                synchronized(DatabaseManager::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                            DatabaseManager::class.java,
                            databaseName).build()
                }
            }

            return instance
        }

        fun releaseInstance() {
            instance = null
        }
    }
}