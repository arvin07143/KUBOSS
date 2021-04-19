package com.example.kuboss.warehouse

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class RackDetailsViewModel(
    val database: WarehouseDatabaseDao,
    application: Application,
    var rackId: String
    ): AndroidViewModel(application) {

    val materialList = database.getMaterials(rackId)
    val allMaterialID = database.getAllMaterialID()
    private var rackList = listOf<String>()

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
    fun getRackList(){
        viewModelScope.launch {
            rackList = database.getRackList()
        }
    }

    fun isRackExist(newRackId: String): Boolean{
        for(rack: String in rackList){
            if(newRackId == rack){
                return true
            }
        }
        return false
    }
}