package com.kay.happyplaces.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kay.happyplaces.models.HappyPlaceModelEntity

@Database(entities = [HappyPlaceModelEntity::class], version = 1, exportSchema = false)
abstract class HappyPlaceDatabase : RoomDatabase() {

    abstract fun happyPlaceDao(): HappyPlaceDao

    companion object {
        @Volatile
        private var INSTANCE: HappyPlaceDatabase? = null

        // get the database
        fun getInstance(context: Context): HappyPlaceDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappyPlaceDatabase::class.java,
                    "happy_place_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}