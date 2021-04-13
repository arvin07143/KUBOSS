package com.example.kuboss.warehouse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentAddUserBinding
import com.example.kuboss.extensions.Extensions.toast
import com.example.kuboss.utils.FirebaseUtils
import com.example.kuboss.views.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_add_user.*


class AddUserFragment : Fragment() {

    var type: String = ""
    lateinit var signInInputsArray: Array<EditText>
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = AddUserViewModelFactory(dataSource)
        val addUserViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(AddUserViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentAddUserBinding>(
            inflater,
            R.layout.fragment_add_user, container, false
        )




        binding.btnAddUser.setOnClickListener{

            if(binding.rbManager.isChecked){
                type = "manager"
            }else if(binding.rbWorker.isChecked){
                type = "worker"

            }
            if(binding.etEmail.text.toString().isNotEmpty() &&
                binding.etPassword.text.toString().isNotEmpty() &&
                binding.etName.text.toString().isNotEmpty() &&
                binding.etConfirmPassword.text.toString().isNotEmpty()&&
                binding.etPassword.text.toString() == binding.etConfirmPassword.text.toString()&&
                    type != "") {

                if ((binding.etEmail.text.toString().trim().matches(emailPattern.toRegex()))) {
                    FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(
                        binding.etEmail.text.toString().trim(),
                        binding.etPassword.text.toString().trim()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Successfully added!", Toast.LENGTH_SHORT)
                                    .show()
                                addUserViewModel.onAddUser(
                                    binding.etEmail.text.toString(),
                                    binding.etPassword.text.toString(),
                                    binding.etName.text.toString(),
                                    type
                                )
                                type = ""
                            } else {
                                Toast.makeText(
                                    activity,
                                    "failed to Authenticate !",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                } else{
                    Toast.makeText(activity, "Invalid email.", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
                    Toast.makeText(activity, "Password not match", Toast.LENGTH_SHORT).show()
                } else if(type == ""){
                    Toast.makeText(activity, "Please choose user type.", Toast.LENGTH_SHORT).show()
                }
                signInInputsArray = arrayOf(binding.etName, binding.etConfirmPassword, binding.etPassword, binding.etEmail)
                signInInputsArray.forEach { input ->
                    if (input.text.toString().isEmpty()) {
                        Toast.makeText(activity, "${input.hint} is required", Toast.LENGTH_SHORT).show()

                    }
                }

            }






        }
        return binding.root
    }




}