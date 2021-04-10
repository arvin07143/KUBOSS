package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Material
import com.example.kuboss.database.RackWithMaterials
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class RackDetailsViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
    ): AndroidViewModel(application) {

    var rackId = ""
    var materialList: List<RackWithMaterials>? = null
    fun onShowRackDetails(){
        viewModelScope.launch {
            materialList = database.showMaterial(rackId)
        }
    }





}