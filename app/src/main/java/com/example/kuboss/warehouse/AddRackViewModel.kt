package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Rack
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch

class AddRackViewModel(
    val database: WarehouseDatabaseDao
): ViewModel() {
    fun onAddRack(rackId: String){
        viewModelScope.launch {
            val newRack = Rack(rackId)
            insert(newRack)

        }
    }
    private suspend fun insert(rack: Rack){
        database.insert(rack)
    }

}