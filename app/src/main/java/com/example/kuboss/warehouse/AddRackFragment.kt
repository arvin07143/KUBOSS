package com.example.kuboss.warehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentAddRackBinding
import com.example.kuboss.databinding.FragmentWarehouseBinding

class AddRackFragment : Fragment() {
    private var binding: FragmentAddRackBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentAddRackBinding.inflate(inflater, container, false)

        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = AddRackViewModelFactory(dataSource)
        val addRackViewModel = ViewModelProvider(
            this, viewModelFactory).get(AddRackViewModel::class.java)

        binding?.addRackViewModel = addRackViewModel

        //set confirm button listener
        binding!!.confirmBtn.setOnClickListener {
            val newRackId = binding!!.editTextAisle.text.toString() + "-" + binding!!.editTextUnit.text.toString()
            addRackViewModel.onAddRack(newRackId)
            findNavController().navigate(R.id.action_addRackFragment_to_warehouseFragment)

        }
        binding!!.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addRackFragment_to_warehouseFragment)
        }

    }


}