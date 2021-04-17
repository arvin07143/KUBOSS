package com.example.kuboss.warehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.database.User
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentEditProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class EditProfileFragment : Fragment() {
    lateinit var epEmail: String
    lateinit var epPassword: String
    lateinit var epConfirmPassword: String
    lateinit var epName: String
    lateinit var epInputsArray: Array<EditText>
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
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

        val currentUser = FirebaseAuth.getInstance().getCurrentUser()
        if (currentUser != null) {
            currentUsername = currentUser.displayName
            currentUserEmail = currentUser.email

        }


        binding.etInputEPEmail.setText(currentUserEmail)
        binding.etInputEPName.setText(currentUsername)


        binding.btnEPSave.setOnClickListener {
            epEmail = binding.etEPEmail.editText?.text.toString().trim()

            epPassword = binding.etEPPassword.editText?.text.toString().trim()
            epConfirmPassword = binding.etEPConfirmPassword.editText?.text.toString().trim()
            epName = binding.etEPName.editText?.text.toString().trim()





            fun notEmpty(): Boolean = (epEmail.isNotEmpty() && epPassword.isNotEmpty()&&epConfirmPassword.isNotEmpty()&&epName.isNotEmpty())

            if (notEmpty() && epPassword.length > 5 && epPassword ==epConfirmPassword) {

                if(epEmail.matches(emailPattern.toRegex())){
                    var success = ""
                    var notSuccess=""

                    val credential = EmailAuthProvider.getCredential(currentUserEmail, epPassword)

                    if (currentUser != null) {
                        currentUser.reauthenticate(credential)
                            .addOnCompleteListener(object: OnCompleteListener<Void> {
                                override fun onComplete(@NonNull task: Task<Void>) {

                                    currentUser.updatePassword(epPassword)
                                        .addOnCompleteListener(object :
                                            OnCompleteListener<Void> {
                                            override fun onComplete(@NonNull task: Task<Void>) {
                                                if (task.isSuccessful()) {

                                                    Log.d("TAG", "Password Updated.")
                                                    success += " password"
                                                } else {
                                                    notSuccess += " password"
                                                }
                                            }
                                        })
                                }
                            })

                        if(epEmail !=currentUserEmail) {
                            currentUser.updateEmail(epEmail)
                                .addOnCompleteListener(object : OnCompleteListener<Void> {
                                    override fun onComplete(@NonNull task: Task<Void>) {
                                        if (task.isSuccessful()) {
                                            editProfileViewModel.updateEmail(
                                                epEmail,
                                                currentUsername
                                            )
                                            Log.d("TAG", "Email Updated.")
                                            success += " email"

                                        } else {
                                            Log.d("TAG", "Email exist.")
                                            notSuccess = " email"
                                        }
                                    }
                                })
                        }
                        if(epName != currentUsername) {
                            currentUser.updateProfile(
                                UserProfileChangeRequest.Builder().setDisplayName(epName).build()
                            )
                                .addOnCompleteListener(object : OnCompleteListener<Void> {
                                    override fun onComplete(@NonNull task: Task<Void>) {
                                        if (task.isSuccessful()) {
                                            editProfileViewModel.updateName(epName, currentUsername)
                                            success += " name"
                                            Log.d("TAG", "name Updated.")


                                        } else {
                                            success = " name"
                                        }
                                    }
                                })
                        }

                        if(success =="" && notSuccess==""){
                            Toast.makeText(activity, "Successfully updated!" , Toast.LENGTH_SHORT)
                                .show()
                        }else if((success !="" && notSuccess!="")) {
                            Toast.makeText(
                                activity,
                                "Successfully update" + success + "," + notSuccess + "update error.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }else if((success !="" && notSuccess =="")){
                            Toast.makeText(activity, "Successfully updated!" , Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(
                                activity,
                                notSuccess + "update error. Maybe email areadly exist.",
                                Toast.LENGTH_SHORT)

                        }

                    }
                }
            } else {
                epInputsArray = arrayOf(
                    binding.etEPEmail.editText!!,
                    binding.etEPPassword.editText!!,
                    binding.etEPConfirmPassword.editText!!,
                    binding.etEPName.editText!!
                )
                //check if any input is empty
                epInputsArray.forEach { input ->
                    if (input.text.toString().isEmpty()) {
                        input.error = "${input.hint} is required"
                    }
                }
                if(epPassword != epConfirmPassword){
                    Toast.makeText(activity, "Password not match!", Toast.LENGTH_SHORT).show()

                }else if(epPassword.length < 6){
                    Toast.makeText(activity, "Password need to have at least 6 character!", Toast.LENGTH_SHORT).show()

                }
            }
        }
        return binding.root
    }


}