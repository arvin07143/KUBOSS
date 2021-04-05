package com.example.kuboss.warehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentWarehouseBinding

class WarehouseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate binding
        val binding: FragmentWarehouseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_warehouse, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = WarehouseViewModelFactory(dataSource, application)
        val warehouseViewModel = ViewModelProvider(
            this, viewModelFactory).get(WarehouseViewModel::class.java)
        binding.lifecycleOwner = this
        binding.warehouseViewModel = warehouseViewModel


        //setup recycler view
        val adapter = WarehouseRackAdapter()
        binding.rackRecyclerView.adapter = adapter
        warehouseViewModel.racks.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.data = it
            }
        })

        //button listener
        binding.addRackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_warehouseFragment_to_addRackFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}