package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WarehouseDatabaseDao {
    @Insert
    suspend fun insert(rack: Rack)

    @Update
    suspend fun update(rack: Rack)

//    @Delete
//    suspend fun delete(rack: Rack)
//
//    @Delete
//    suspend fun deleteAll()

    @Query("SELECT * FROM rack_table ORDER BY rackId")
    fun getAllRacks(): LiveData<List<Rack>>
}