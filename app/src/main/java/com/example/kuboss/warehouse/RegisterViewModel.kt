package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch


class RegisterViewModel(val database: WarehouseDatabaseDao): ViewModel() {

    fun onAddUser(email: String, password:String, name:String){
        //add new user into local database
        viewModelScope.launch {
            val newUser = User(email, password, name)
            database.addUser(newUser)

        }
    }

}