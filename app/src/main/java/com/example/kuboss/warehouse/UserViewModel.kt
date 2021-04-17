package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {
    var userEmail:String=""
    fun getUserEmail(name:String):String{

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                userEmail = database.getEmailByName(name)

            }

            job.join()
        }
        return userEmail
    }
    fun onDeleteUser(name:String){
        viewModelScope.launch {
            database.removeUserByName(name)
        }
    }



}