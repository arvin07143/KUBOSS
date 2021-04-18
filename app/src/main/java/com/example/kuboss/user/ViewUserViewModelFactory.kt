package com.example.kuboss.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class ViewUserViewModelFactory (
    private val dataSource: WarehouseDatabaseDao,
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewUserViewModel::class.java)) {
            return ViewUserViewModel(dataSource, application) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}