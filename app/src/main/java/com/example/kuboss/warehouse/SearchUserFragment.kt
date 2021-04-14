package com.example.kuboss.warehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.adapter.ViewUserAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentSearchUserBinding


class SearchUserFragment : Fragment() {

    companion object {
        fun newInstance() = ViewUserFragment()
    }

    private lateinit var viewModel: SearchUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSearchUserBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_user,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = SearchUserViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(SearchUserViewModel::class.java)
        binding.lifecycleOwner = this
        binding.searchUserViewModel = viewModel


        val adapter = ViewUserAdapter()
        binding.searchUser.adapter = adapter
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.dataset = it
                Log.e("Test","Update")
            }
        })

        binding.searchField.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchStringChange("%"+binding.searchField.editText?.text.toString()+"%")
                true
            } else {
                false
            }
        }

        binding.searchField.setEndIconOnClickListener {
            viewModel.searchStringChange("%"+binding.searchField.editText?.text.toString()+"%")
            Log.e("Test","Search Done")
        }

        return binding.root
    }


}