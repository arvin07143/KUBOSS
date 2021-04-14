package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class ReceiveItemViewModelFactory(
    private val dataSource: WarehouseDatabaseDao,
    private val application: Application,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiveItemViewModel::class.java)) {
            return ReceiveItemViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}