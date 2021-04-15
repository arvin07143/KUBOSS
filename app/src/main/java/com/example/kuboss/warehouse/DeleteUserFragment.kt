package com.example.kuboss.warehouse

import android.content.Intent
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.MainActivity
import com.example.kuboss.R
import com.example.kuboss.adapter.deleteUserAdapter
import com.example.kuboss.adapter.ViewUserAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentDeleteUserBinding
import com.example.kuboss.extensions.Extensions.toast
import com.example.kuboss.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_rack_details.*


class DeleteUserFragment : Fragment() {
    lateinit var selectedUserForDelete: MutableList<String>
    lateinit var email: String
    lateinit var password: String

        companion object {
            fun newInstance() = DeleteUserFragment()
        }

        private lateinit var viewModel: DeleteUserViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val binding: FragmentDeleteUserBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_delete_user,container,false)
            val application = requireNotNull(this.activity).application
            val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
            val viewModelFactory = DeleteUserViewModelFactory(dataSource, application)
            viewModel = ViewModelProvider(
                this, viewModelFactory).get(DeleteUserViewModel::class.java)
            binding.lifecycleOwner = this
            binding.deleteUserViewModel = viewModel


            val adapter = deleteUserAdapter()
            binding.deleteUser.adapter = adapter
            viewModel.searchResults.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.dataset = it
                    Log.e("Test","Update")
                }
            })

            binding.btnRemoveUser.setOnClickListener{
//                val currentUser = FirebaseAuth.getInstance().getCurrentUser()
//                if(currentUser == null){
//                    Toast.makeText(activity, "empty", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(activity, "successfully", Toast.LENGTH_SHORT).show()
//                }
                selectedUserForDelete = adapter.returnUserForDelete()

                selectedUserForDelete.forEach { user ->

                    email = viewModel.getUserEmail(user)
                    password = viewModel.getUserPassword(user)
//                    Toast.makeText(activity, email + password, Toast.LENGTH_SHORT).show()
//                    FirebaseUtils.firebaseAuth.signOut()
//                    FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(email, password)
//                    val currentUser = FirebaseAuth.getInstance().getCurrentUser()
//                    if(currentUser == null){
//                        Toast.makeText(activity, "empty", Toast.LENGTH_SHORT).show()
//                    }else{
//                        Toast.makeText(activity, "successfully", Toast.LENGTH_SHORT).show()
//                    }
//                        .addOnCompleteListener { signIn ->
//                            if (signIn.isSuccessful) {
//                                //startActivity(Intent(this, HomeActivity::class.java))
//                                Toast.makeText(activity, "successfully", Toast.LENGTH_SHORT).show()
//
//                            } else {
//                                Toast.makeText(activity, "invalid", Toast.LENGTH_SHORT).show()
//
//                            }
//                        }

                    //viewModel.firebaseRemove(email,password)
                   //viewModel.onDeleteUser(user)
                }
                FirebaseUtils.firebaseAuth.signOut()
                FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(email, password)
                val currentUser = FirebaseAuth.getInstance().getCurrentUser()
                if(currentUser == null){
                    Toast.makeText(activity, "empty", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "successfully", Toast.LENGTH_SHORT).show()
                }

            }


            return binding.root
        }


    }