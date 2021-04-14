package com.example.kuboss.warehouse

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.R
import com.example.kuboss.database.Rack
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import com.example.kuboss.extensions.Extensions.toast
import com.example.kuboss.utils.FirebaseUtils
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity

import com.example.kuboss.extensions.Extensions.toast
import com.example.kuboss.utils.FirebaseUtils.firebaseAuth
import com.example.kuboss.views.HomeActivity


class AddUserViewModel(
    val database: WarehouseDatabaseDao
): ViewModel() {


    fun onAddUser(email: String, password:String, name:String,type: String){
        viewModelScope.launch {
            val newUser = User(email, password, name, type)
            //database.addUser(newUser)
        }
    }

}