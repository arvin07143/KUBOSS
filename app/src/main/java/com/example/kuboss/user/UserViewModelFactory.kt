package com.example.kuboss.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class UserViewModelFactory (
    private val dataSource: WarehouseDatabaseDao,
    private val application: Application,
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(dataSource, application) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}