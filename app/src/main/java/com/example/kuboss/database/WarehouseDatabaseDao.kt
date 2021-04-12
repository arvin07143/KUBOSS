package com.example.kuboss.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface WarehouseDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(rack: Rack)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(material: Material)

    @Update
    suspend fun update(rack: Rack)

    @Query("DELETE FROM rack_table WHERE rackId = :rackID")
    suspend fun removeRack(rackID: String)

    @Query("SELECT * FROM rack_table WHERE rackId=:rackID")
    suspend fun getRack(rackID: String): Rack

    @Transaction
    @Query("SELECT * FROM rack_table WHERE rackId=:rackID")
    fun getMaterials(rackID: String): LiveData<RackWithMaterials>

    @Query("UPDATE rack_table SET rackId=:newRackID WHERE rackId=:oldRackID ")
    suspend fun updateRack(oldRackID: String, newRackID: String)

    @Query("UPDATE material_table SET mRackId=:newRackID WHERE mRackId=:oldRackID")
    suspend fun updateMaterialRack(oldRackID: String, newRackID: String)

    @Query("SELECT * FROM rack_table ORDER BY rackId")
    fun getAllRacks(): LiveData<List<Rack>>
}