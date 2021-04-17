package com.example.kuboss.warehouse

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditProfileViewModel(val database: WarehouseDatabaseDao): ViewModel() {

    lateinit var user: User


    fun updateEmail(newEmail:String, oldName:String){

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                database.updateUserEmail(newEmail, oldName)

            }

            job.join()
        }

    }

    fun updateName(newName:String, oldName:String){

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                database.updateUserName(newName, oldName)

            }

            job.join()
        }

    }



}