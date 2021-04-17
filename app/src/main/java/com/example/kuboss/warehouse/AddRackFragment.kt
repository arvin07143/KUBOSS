package com.example.kuboss.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentAddRackBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        val viewModelFactory = WarehouseViewModelFactory(dataSource, application)
        val addRackViewModel = ViewModelProvider(
            requireActivity().viewModelStore, viewModelFactory
        ).get(WarehouseViewModel::class.java)

        //set confirm button listener
        binding!!.confirmBtn.setOnClickListener {
            val aisle = binding!!.editTextAisle.text.toString()
            val unit = binding!!.editTextUnit.text.toString()
            val newRackId = when {
                (aisle != "" && unit == "") -> aisle
                (aisle == "" && unit != "") -> unit
                (aisle != "" && unit != "") -> "$aisle-$unit"
                else -> ""
            }

            if (newRackId == "") {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("Aisle and Unit cannot be empty")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                    .show()
            } else {
                addRackViewModel.onAddRack(newRackId)
            }

        }
        binding!!.cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addRackFragment_to_warehouseFragment)
        }

        addRackViewModel.isSqlError.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Rack")
                    .setMessage("This rack already exist.")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                    .show()
                addRackViewModel.finishShowingDialog()
            }
        })

        addRackViewModel.isAddRackSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Rack")
                    .setMessage("Rack added successfully.")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->

                    }
                    .show()
                addRackViewModel.finishShowingDialog()
            }
        })

    }


}