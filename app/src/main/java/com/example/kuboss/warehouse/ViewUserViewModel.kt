package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.WarehouseDatabaseDao

class ViewUserViewModel (
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {

    val searchResults = database.getAllUsers()

}







