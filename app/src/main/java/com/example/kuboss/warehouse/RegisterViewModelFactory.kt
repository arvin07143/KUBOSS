package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class RegisterViewModelFactory(private val dataSource: WarehouseDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(dataSource) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}