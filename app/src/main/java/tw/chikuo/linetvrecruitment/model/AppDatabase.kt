package tw.chikuo.linetvrecruitment.model

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.content.Context


@Database(entities = [(Drama::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var sInstance: AppDatabase? = null
        private const val DB_NAME = "ANDROID_DB"


        fun getDatabase(context: Context): AppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                        DB_NAME)
                        .fallbackToDestructiveMigration()   //  Debug use only
                        .build()
            }
            return sInstance as AppDatabase
        }
    }

    abstract fun dramaDao(): DramaDao
}
