package com.example.kuboss.warehouse

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabaseDao
import com.example.kuboss.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class DeleteUserViewModel(
    val database: WarehouseDatabaseDao,
    application: Application
) : ViewModel() {
    val searchResults = database.getAllUsers()
    var email:String =""
    var password:String=""

    fun onDeleteUser(name:String){
        viewModelScope.launch {
            database.removeUserByName(name)
        }
    }

    fun getUserEmail(name:String):String{

            runBlocking {

                val job: Job = launch(context = Dispatchers.Default) {
                    email = database.getEmailByName(name)

                }

                job.join()
            }
        return email
    }

    fun getUserPassword(name:String):String{

        runBlocking {

            val job: Job = launch(context = Dispatchers.Default) {
                password = database.getPasswordByName(name)

            }

            job.join()
        }
        return password
    }


    fun firebaseRemove(email:String, password:String){

        val currentUser = FirebaseAuth.getInstance().getCurrentUser()
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider.getCredential(email, password)
        // Prompt the user to re-provide their sign-in credentials
        if (currentUser != null)
        {
            currentUser.reauthenticate(credential)
                .addOnCompleteListener(object: OnCompleteListener<Void> {
                    override fun onComplete(@NonNull task: Task<Void>) {
                        currentUser.delete()
                            .addOnCompleteListener(object: OnCompleteListener<Void> {
                                override fun onComplete(@NonNull task: Task<Void>) {
                                    if (task.isSuccessful())
                                    {
                                        Log.d("TAG", "User account deleted.")

                                    }
                                }
                            })
                    }
                })
        }else{
            Log.d("TAG", "User account empty.")
        }
    }


















}