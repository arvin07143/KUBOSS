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

    fun storeMaterial(mat: Material): LiveData<Boolean> {
        val error = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                database.insert(mat)
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



}