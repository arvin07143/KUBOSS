package com.example.kuboss.warehouse

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kuboss.database.Material
import com.example.kuboss.database.Rack
import com.example.kuboss.database.RackWithMaterials
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class RackDetailsViewModel(
    val database: WarehouseDatabaseDao,
    application: Application,
    var rackId: String
    ): AndroidViewModel(application) {

    val materialList = database.getMaterials(rackId)


    fun storeMaterial(mat: Material){
        viewModelScope.launch {
            database.insert(mat)
            Log.e("DB","ADD SUCCESSFUL")
        }
    }



}