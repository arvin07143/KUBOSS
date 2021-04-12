package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Rack
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class EditRackViewModel(
    val database: WarehouseDatabaseDao
): ViewModel() {

    var rackId = ""

    fun onDeleteRack(){
        viewModelScope.launch {
            database.removeRack(rackId)
        }
    }

    fun updateRack(oldRackId: String, newRackId: String){
        viewModelScope.launch {
            database.updateRack(oldRackId, newRackId)
            database.updateMaterialRack(oldRackId, newRackId)
        }
    }
}