package com.example.kuboss.user

import androidx.lifecycle.ViewModel
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel (val database: WarehouseDatabaseDao): ViewModel() {
    var currentUser:String? = ""

    fun checkUserExist(userEmail:String, userPassword:String, userName:String):String?{

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {

                    currentUser = database.checkUserExist(userEmail, userName)
            }


            job.join()
        }
        return currentUser

    }

    fun addUser(userEmail:String, userName:String){

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {

                database.addUser(User(userEmail,userName))

            }

            job.join()
        }


    }

}
