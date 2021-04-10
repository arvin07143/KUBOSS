package com.example.kuboss.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Rack::class, Material::class], version = 1, exportSchema = false)
abstract class WarehouseDatabase : RoomDatabase() {

    abstract val warehouseDatabaseDao: WarehouseDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: WarehouseDatabase? = null

        fun getInstance(context: Context): WarehouseDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WarehouseDatabase::class.java,
                        "warehouse_database"
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