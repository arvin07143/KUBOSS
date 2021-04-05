package com.example.kuboss.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Rack::class], version = 1, exportSchema = false)
abstract class RackDatabase : RoomDatabase() {
    abstract val rackDatabaseDao: RackDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: RackDatabase? = null
        fun getInstance(context: Context): RackDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RackDatabase::class.java,
                        "rack_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}