package com.example.kuboss.warehouse

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentUserBinding
import com.example.kuboss.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class UserFragment : Fragment() {
    var currentUsername: String = ""
    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentUserBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = UserViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(UserViewModel::class.java)
        binding.lifecycleOwner = this
        binding.userViewModel = viewModel

        val currentUser = FirebaseAuth.getInstance().getCurrentUser()
        if (currentUser != null) {
            currentUsername = currentUser.displayName

        }

        val currentUserEmail = viewModel.getUserEmail(currentUsername)

        binding.name.text = currentUsername
        binding.email.text = currentUserEmail


        binding.btnEditProfile.setOnClickListener{
            findNavController().navigate(R.id.action_userFragment_to_editProfileFragment)
        }

        binding.btnViewUser.setOnClickListener{

                findNavController().navigate(R.id.action_userFragment_to_viewUserFragment)

        }

        binding.btnRemoveAccount.setOnClickListener{
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("Are you sure you want to remove?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        if (currentUser != null)
                        {
                            //remove from firebase
                            currentUser.delete()
                                .addOnCompleteListener(object: OnCompleteListener<Void> {
                                    override fun onComplete(@NonNull task: Task<Void>) {
                                        if (task.isSuccessful())
                                        {
                                            //remove from local database
                                            viewModel.onDeleteUser(currentUsername)

                                            findNavController().navigate(R.id.action_userFragment_to_loginActivity)
                                            Toast.makeText(activity, "User account deleted!", Toast.LENGTH_SHORT).show()


                                        }else{
                                            Toast.makeText(activity, "Fail to deleted.!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })

                        }else{
                            Toast.makeText(activity, "User account empty.", Toast.LENGTH_SHORT).show()

                        }

                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }


        binding.btnSignout.setOnClickListener{
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    if (currentUser != null)
                    {
                        FirebaseUtils.firebaseAuth.signOut()
                        findNavController().navigate(R.id.action_userFragment_to_loginActivity)
                        Toast.makeText(activity, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity, "User account empty.", Toast.LENGTH_SHORT).show()

                    }

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }


        return binding.root

    }


}