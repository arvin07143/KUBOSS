package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RackDatabaseDAO {
    @Insert
    fun insert(newRack: Rack)

    @Update
    fun update(changeRack: Rack)

    @Query("SELECT rack_items FROM rack_table WHERE rackID = :key")
    fun getItemList(key: Int): LiveData<String>
}