package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WarehouseDatabaseDao {
    @Insert
    suspend fun insert(rack: Rack)

    @Update
    suspend fun update(rack: Rack)

    @Query("DELETE FROM rack_table WHERE rackId = :rackID")
    suspend fun removeRack(rackID: String)

    @Transaction
    @Query("SELECT * FROM rack_table WHERE rackId=:rackID")
    suspend fun showMaterial(rackID: String): List<RackWithMaterials>

    @Query("UPDATE rack_table SET rackId=:newRackID WHERE rackId=:oldRackID ")
    suspend fun updateRack(oldRackID: String, newRackID: String)

    @Query("SELECT * FROM rack_table ORDER BY rackId")
    fun getAllRacks(): LiveData<List<Rack>>
}