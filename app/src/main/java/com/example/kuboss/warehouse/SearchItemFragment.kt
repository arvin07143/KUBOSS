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
import com.example.kuboss.R
import com.example.kuboss.adapter.SearchItemAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.SearchItemFragmentBinding

class SearchItemFragment : Fragment() {

    companion object {
        fun newInstance() = SearchItemFragment()
    }

    private lateinit var viewModel: SearchItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SearchItemFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.search_item_fragment,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = SearchItemViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(SearchItemViewModel::class.java)
        binding.lifecycleOwner = this
        binding.searchItemViewModel = viewModel

        val adapter = SearchItemAdapter()
        binding.searchItemDetails.adapter = adapter
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