package com.example.kuboss.warehouse

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kuboss.R
import com.example.kuboss.adapter.SearchItemAdapter
import com.example.kuboss.adapter.ViewUserAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentViewUserBinding


class ViewUserFragment : Fragment() {

    companion object {
        fun newInstance() = ViewUserFragment()
    }

    private lateinit var viewModel: ViewUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.dataset = it
                Log.e("Test","Update")
            }
        })



        return binding.root
    }


}