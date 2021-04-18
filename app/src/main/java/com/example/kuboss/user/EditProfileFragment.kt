package com.example.kuboss.user

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentEditProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class EditProfileFragment : Fragment() {

    lateinit var epPassword: String
    lateinit var epConfirmPassword: String
    lateinit var epName: String
    lateinit var currentUsername:String
    lateinit var currentUserEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = EditProfileViewModelFactory(dataSource)
        val editProfileViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(EditProfileViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentEditProfileBinding>(
            inflater,
            R.layout.fragment_edit_profile, container, false
        )

        //get current user's name and email
        val currentUser = FirebaseAuth.getInstance().getCurrentUser()
        if (currentUser != null) {
            currentUsername = currentUser.displayName
            currentUserEmail = currentUser.email
        }
        //set the edit text of the name to the current user' name
        binding.etInputEPName.setText(currentUsername)


        binding.btnEPSave.setOnClickListener {
            epPassword = binding.etEPPassword.editText?.text.toString().trim()
            epConfirmPassword = binding.etEPConfirmPassword.editText?.text.toString().trim()
            epName = binding.etEPName.editText?.text.toString().trim()

            if (currentUser != null) {
                //for display which is complete updated
                var success = "Successfull Message\n"
                var notSuccess = "\nError Message\n"

                if (epPassword.isNotEmpty() && epConfirmPassword.isNotEmpty()) {
                    if (epPassword.length > 5 && epPassword == epConfirmPassword) {

                        val credential = EmailAuthProvider.getCredential(currentUserEmail, epPassword)
                        currentUser.reauthenticate(credential)
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(@NonNull task: Task<Void>) {
                                    currentUser.updatePassword(epPassword)
                                    }
                                })
                        success += " - Password Updated.\n"

                    } else if (epPassword.length < 6) {
                        notSuccess += " - Password need to have at least 6 character!\n"
                    } else if (epPassword != epConfirmPassword) {
                        notSuccess += " - Password not match!\n"
                    }
                }

                if(epName.isNotEmpty()) {
                    if (epName != currentUsername) {

                        currentUser.updateProfile(
                            UserProfileChangeRequest.Builder().setDisplayName(epName).build()
                        )
                        //update to local database
                        editProfileViewModel.updateName(epName, currentUsername)
                        success += " - Name Updated.\n"
                    }

                }


                val builder = AlertDialog.Builder(activity)
                if(success == "Successfull Message\n" && notSuccess == "\nError Message\n") {
                    builder.setMessage("Nothing is updated.")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()

                        }

                }else if(success != "Successfull Message\n" && notSuccess == "\nError Message\n"){
                    builder.setMessage(success )

                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()

                        }
                }else if(success == "Successfull Message\n" && notSuccess != "\nError Message\n"){
                    builder.setMessage(notSuccess)

                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()

                        }
                }else{
                    builder.setMessage(success + notSuccess)

                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()

                        }


                }
                val alert = builder.create()
                alert.show()


            }

        }
        return binding.root
    }
}





