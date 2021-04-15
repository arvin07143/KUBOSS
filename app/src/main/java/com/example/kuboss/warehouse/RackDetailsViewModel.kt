package com.example.kuboss.warehouse

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.*
import com.example.kuboss.database.Material
import com.example.kuboss.database.Rack
import com.example.kuboss.database.RackWithMaterials
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.lang.Exception

class RackDetailsViewModel(
    val database: WarehouseDatabaseDao,
    application: Application,
    var rackId: String
    ): AndroidViewModel(application) {

    val materialList = database.getMaterials(rackId)
    private lateinit var rack: Rack
    init{
        getRack()
        Log.d("materialList", materialList.value?.materials?.size.toString())
    }
    fun storeMaterial(materialID:String,newRackID:String): LiveData<Int> {
        val error = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                error.value = database.updateMatLocation(materialID,newRackID)
            } catch (e:Exception){
                Log.e("DB",e.toString())
            }
        }
        return error
    }

    fun findRackID(materialID: String):LiveData<String>{
        return database.findRack(materialID)
    }


    fun pickMaterial(pickedMat:Material): LiveData<Int> {
        val error = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                error.value = database.pickMaterial(pickedMat)

            } catch (e:Exception){
                Log.e("DB",e.toString())
            }
        }
        return error
    }

    fun onDeleteRack(){
        viewModelScope.launch {
            moveMaterial()
            remove()
        }
    }
    private suspend fun remove(){
        viewModelScope.launch {
            database.removeRack(rackId)
        }
    }
    private suspend fun moveMaterial(){
        viewModelScope.launch {
            database.moveMaterialOnDeleteRack(rackId)
        }
    }

    fun updateRack(oldRackId: String, newRackId: String){
        viewModelScope.launch {
            try {
                database.updateRack(oldRackId, newRackId)
                database.updateMaterialRack(oldRackId, newRackId)
            }catch (e: SQLiteConstraintException){
            }
        }
    }
    private fun getRack(){
        viewModelScope.launch {
            rack = database.getRack(rackId)
        }
    }

}