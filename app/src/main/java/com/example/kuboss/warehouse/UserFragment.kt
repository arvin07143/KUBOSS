package com.example.kuboss.warehouse

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
    lateinit var currentUsername: String
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
        val currentUserType = viewModel.getUserType(currentUsername)

        if (currentUser != null) {
            currentUsername = currentUser.displayName

        }


        binding.name.text = currentUsername

        if(currentUserType !="") {
            binding.type.text = currentUserType
        }else{
            binding.type.text = "Havent set"
        }

        binding.btnEditProfile.setOnClickListener{

        }

        binding.btnManageUser.setOnClickListener{
            if(currentUserType == "admin") {
                findNavController().navigate(R.id.action_userFragment_to_viewUserFragment)
            }else{
                Toast.makeText(activity, "Only admin is allowed to manage user!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRemoveAccount.setOnClickListener{

            if (currentUser != null)
            {
                currentUser.delete()
                    .addOnCompleteListener(object: OnCompleteListener<Void> {
                        override fun onComplete(@NonNull task: Task<Void>) {
                            if (task.isSuccessful())
                            {
                                //still need to remove from database
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

        binding.btnSignout.setOnClickListener{
            FirebaseUtils.firebaseAuth.signOut()
            findNavController().navigate(R.id.action_userFragment_to_loginActivity)
            Toast.makeText(activity, "Signed out!", Toast.LENGTH_SHORT).show()
        }


        return binding.root

    }


}