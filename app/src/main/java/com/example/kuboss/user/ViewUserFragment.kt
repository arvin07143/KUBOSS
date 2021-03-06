package com.example.kuboss.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.adapter.ViewUserAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentViewUserBinding


class ViewUserFragment : Fragment() {

    companion object {
    }

    private lateinit var viewModel: ViewUserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentViewUserBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_user,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = ViewUserViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(
            this, viewModelFactory).get(ViewUserViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewUserViewModel = viewModel

        val adapter = ViewUserAdapter()
        binding.viewUser.adapter = adapter
        viewModel.searchResults.observe(viewLifecycleOwner, {
            it?.let {
                adapter.dataset = it
                Log.e("Test","Update")

            }
        })


        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_viewUserFragment_to_searchUserFragment)
        }
        return binding.root
    }
}