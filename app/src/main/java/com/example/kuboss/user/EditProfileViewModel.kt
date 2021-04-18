package com.example.kuboss.user

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditProfileViewModel(val database: WarehouseDatabaseDao): ViewModel() {

    lateinit var user: User
    var successMSG:String = "Successfull Message\n"
    var notSuccessMSG:String = "Error Message\n"


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

//    fun setSuccessMsg(success:String){
//        successMSG += success
//    }
//
//    fun setFailMsg(fail:String){
//        notSuccessMSG += fail
//    }

    fun getSuccessMsg():String{
        return successMSG
    }

    fun getFailMsg():String{
        return notSuccessMSG
    }

    fun clearMsg(){
        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                successMSG = "Successfull Message\n"
                notSuccessMSG = "Error Message\n"

            }

            job.join()
        }
    }

    fun editPassword(currentUser:FirebaseUser, credential:AuthCredential, epPassword:String):Boolean {
         var success:Boolean = false

                currentUser.reauthenticate(credential)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(@NonNull task: Task<Void>) {
                            currentUser.updatePassword(epPassword)
                                .addOnCompleteListener(object :
                                    OnCompleteListener<Void> {
                                    override fun onComplete(@NonNull task: Task<Void>) {
                                        if (task.isSuccessful()) {
                                            success = true
                                        } else {
                                            success = false
                                        }
                                    }
                                })

                        }

                    })


        return success
    }
}