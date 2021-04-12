package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class RackDetailsViewModelFactory(
    private val dataSource: WarehouseDatabaseDao,
    private val application: Application,
    private val rackId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RackDetailsViewModel::class.java)) {
            return RackDetailsViewModel(dataSource, application, rackId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}