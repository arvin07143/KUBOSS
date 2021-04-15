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
    private var rackList = listOf<String>()

    fun storeMaterial(materialID:String,newRackID:String): LiveData<Boolean> {
        val error = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                database.updateMatLocation(materialID,newRackID)
                error.value = false
            } catch (e:Exception){
                Log.e("DB",e.toString())
                error.value = true
            }
        }
        return error
    }


    fun pickMaterial(pickedMat:Material): LiveData<Int> {
        val error = MutableLiveData<Int>()
        viewModelScope.launch {
            try {
                error.value = database.pickMaterial(pickedMat)

            } catch (e:Exception){
                Log.e("DB",e.toString())
                Log.e("test",error.value.toString())
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
                Log.d("exist", newRackId)
                return true
            }
        }
        return false
    }
}