package com.example.kuboss.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.database.WarehouseDatabaseDao

class SearchUserViewModelFactory (
        private val dataSource: WarehouseDatabaseDao,
        private val application: Application,
    ) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchUserViewModel::class.java)) {
                return SearchUserViewModel(dataSource, application) as T

            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
