package com.example.kuboss.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.launch


class RegisterViewModel(val database: WarehouseDatabaseDao): ViewModel() {

    fun onAddUser(email: String, name:String){
        //add new user into local database
        viewModelScope.launch {
            val newUser = User(email, name)
            database.addUser(newUser)

        }
    }

}