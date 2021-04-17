package com.example.kuboss.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentRegisterUserBinding
import com.example.kuboss.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterFragment :  Fragment(){
    lateinit var registerEmail: String
    lateinit var registerPassword: String
    lateinit var registerConfirmPassword: String
    lateinit var registerName: String
    lateinit var registerInputsArray: Array<EditText>
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = RegisterViewModelFactory(dataSource)
        val addUserViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(RegisterViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentRegisterUserBinding>(
            inflater,
            R.layout.fragment_register_user, container, false
        )

        binding.tvReturnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
        }

        binding.btnAddUser.setOnClickListener {
            registerEmail = binding.etRegisterEmail.editText?.text.toString().trim()
            registerPassword = binding.etRegisterPassword.editText?.text.toString().trim()
            registerConfirmPassword = binding.etRegisterConfirmPassword.editText?.text.toString().trim()
            registerName = binding.etRegisterName.editText?.text.toString().trim()

            fun notEmpty(): Boolean = (registerEmail.isNotEmpty() && registerPassword.isNotEmpty()&&registerConfirmPassword.isNotEmpty()&&registerName.isNotEmpty())
            if (notEmpty() && registerPassword.length > 5 && registerPassword ==registerConfirmPassword) {
                if(registerEmail.matches(emailPattern.toRegex())){
                    //add into firebase
                    FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(
                        registerEmail,
                        registerPassword
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //add into local database
                                addUserViewModel.onAddUser(
                                    registerEmail,
                                    registerPassword,
                                    registerName
                                )
                                val user = FirebaseAuth.getInstance().currentUser

                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(registerName).build()

                                user.updateProfile(profileUpdates)
                                findNavController().navigate(R.id.action_registerFragment2_to_mainActivity)
                                FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(
                                    registerEmail,
                                    registerPassword
                                )
                                Toast.makeText(activity, "Successfully added!", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(activity, "Email already exist!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    Toast.makeText(activity, "Invalid email!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if(registerPassword != registerConfirmPassword){
                    Toast.makeText(activity, "Password not match!", Toast.LENGTH_SHORT).show()

                }else if(registerPassword.length < 6){
                    Toast.makeText(
                        activity,
                        "Password need to have at least 6 character!",
                        Toast.LENGTH_SHORT
                    ).show()


                }else {
                    //put in all the input
                    registerInputsArray = arrayOf(
                        binding.etRegisterEmail.editText!!,
                        binding.etRegisterPassword.editText!!,
                        binding.etRegisterConfirmPassword.editText!!,
                        binding.etRegisterName.editText!!
                    )
                    //check if any input is empty
                    registerInputsArray.forEach { input ->
                        if (input.text.toString().isEmpty()) {
                            input.error = "${input.hint} is required"
                        }
                    }

                }
            }
        }
        return binding.root
    }
}