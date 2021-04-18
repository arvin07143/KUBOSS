package com.example.kuboss.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class LoginViewModelFactory(private val dataSource: WarehouseDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataSource) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}