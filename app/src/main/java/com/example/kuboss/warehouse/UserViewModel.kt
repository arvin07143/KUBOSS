package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {
    var userType:String=""
    fun getUserType(name:String):String{

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                    userType = database.getuserTypeByName(name)

            }

            job.join()
        }
        return userType
    }



}