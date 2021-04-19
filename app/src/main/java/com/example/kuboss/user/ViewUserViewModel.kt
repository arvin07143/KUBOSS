package com.example.kuboss.user

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.WarehouseDatabaseDao

class ViewUserViewModel (
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {

    val searchResults = database.getAllUsers()

}







