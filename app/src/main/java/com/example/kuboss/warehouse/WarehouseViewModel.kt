package com.example.kuboss.warehouse

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.Rack
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch


class WarehouseViewModel(
    val database: WarehouseDatabaseDao,
    application: Application): AndroidViewModel(application) {

    val racks = database.getAllRacks()

    init{
//        getMap()
        Log.d("warehouseVM", "init")
    }







}