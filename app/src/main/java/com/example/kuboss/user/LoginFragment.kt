package com.example.kuboss.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
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
import com.example.kuboss.databinding.FragmentLoginBinding
import com.example.kuboss.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>
    var currentUsername: String = ""
    var currentUserProfileEmail:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = LoginViewModelFactory(dataSource)
        val loginViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(LoginViewModel::class.java)




        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.tvForgetPassword.setOnClickListener {
            var resetEmail:String
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Please enter your email.")

            val input = EditText(activity)

            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            builder.setView(input)

            builder.setPositiveButton("Send",
                DialogInterface.OnClickListener { dialog, which ->
                    resetEmail = input.text.toString()
                    FirebaseAuth . getInstance ().sendPasswordResetEmail(resetEmail)
                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Email sent.", Toast.LENGTH_SHORT).show()

                            }
                        })
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }


        binding.btnLogin.setOnClickListener {
            signInEmail = binding.etLoginEmail.editText?.text.toString().trim()
            signInPassword = binding.etLoginPassword.editText?.text.toString().trim()

            fun notEmpty(): Boolean = (signInEmail.isNotEmpty() && signInPassword.isNotEmpty())

            if (notEmpty()) {
                //sign in with firebase
                FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                    .addOnCompleteListener { signIn ->
                        if (signIn.isSuccessful) {
                            val currentUser = FirebaseAuth.getInstance().getCurrentUser()
                            if (currentUser != null) {
                                currentUsername = currentUser.displayName

                            }
                            currentUserProfileEmail = loginViewModel.checkUserExist(
                                signInEmail,
                                signInPassword,
                                currentUsername
                            )
                            //check if the current user' record exist in database if no then add in
                            if(currentUserProfileEmail == null){
                                loginViewModel.addUser(signInEmail, currentUsername)
                            }
                            //navigate to main activity
                            findNavController().navigate(R.id.action_loginFragment2_to_mainActivity)
                            Toast.makeText(activity, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                            this.requireActivity().finish()

                        } else {
                            Toast.makeText(
                                activity,
                                "Invalid email and password combination!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            } else {
                signInInputsArray = arrayOf(
                    binding.etLoginEmail.editText!!,
                    binding.etLoginPassword.editText!!
                )
                signInInputsArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"

                    }
                }
            }

        }

        binding.tvNavigateToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }

        return binding.root
    }

}