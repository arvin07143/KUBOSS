package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel (val database: WarehouseDatabaseDao): ViewModel() {
    lateinit var currentUser:User

    fun checkUserExist(userEmail:String, userPassword:String, userName:String):User{

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                currentUser=database.checkUserExist(userEmail, userPassword,userName)

            }

            job.join()
        }
        return currentUser

    }

    fun addUser(userEmail:String, userPassword:String, userName:String){

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {

                database.addUser(User(userEmail, userPassword,userName))

            }

            job.join()
        }


    }

}
