package com.example.kuboss.warehouse

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.LiveBarcodeScanningActivity
import com.example.kuboss.R
import com.example.kuboss.adapter.WarehouseRackAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentWarehouseBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            requireActivity().viewModelStore, viewModelFactory).get(WarehouseViewModel::class.java)
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

        //setup floating action button
        val btnAdd: FloatingActionButton = binding.expandableFabLayoutHome.findViewById(R.id.add_rack_btn)
        btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_warehouseFragment_to_addRackFragment)
        }

        val btnReceive:FloatingActionButton = binding.expandableFabLayoutHome.findViewById(R.id.receive_mat_btn)
        btnReceive.setOnClickListener {
            findNavController().navigate(R.id.action_warehouseFragment_to_receiveMatFragment)
        }
        val btnMap: FloatingActionButton = binding.expandableFabLayoutHome.findViewById(R.id.view_map_btn)
        btnMap.setOnClickListener {
            findNavController().navigate(R.id.action_warehouseFragment_to_warehouseMapFragment)
        }

        return binding.root
    }

}