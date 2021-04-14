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

    @Query("SELECT * FROM material_table WHERE sku LIKE :searchSKU")
    fun searchBySKU(searchSKU: String): LiveData<List<Material>>

    @Delete
    suspend fun pickMaterial(pickedMat:Material):Int

    @Query("SELECT materialId FROM material_table")
    fun getAllMaterialID(): LiveData<List<String>>

    @Transaction
    @Query("UPDATE material_table SET mRackId=:newRackID WHERE materialId=:materialID")
    suspend fun updateMatLocation(materialID:String,newRackID: String)

    @Insert
    suspend fun addUser(user: User)

    @Query("DELETE FROM user_table WHERE name = :userName")
    suspend fun removeUser(userName: String)

    @Query("SELECT * FROM user_table ORDER BY name")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE name LIKE :userName")
    fun searchByUserName(userName: String): LiveData<List<User>>




}