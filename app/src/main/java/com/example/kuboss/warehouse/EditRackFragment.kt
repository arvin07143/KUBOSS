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
import androidx.navigation.fragment.navArgs
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentEditRackBinding

class EditRackFragment : Fragment() {

    val args: EditRackFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentEditRackBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_rack, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = EditRackViewModelFactory(dataSource)
        val editRackViewModel = ViewModelProvider(
            this, viewModelFactory).get(EditRackViewModel::class.java)

        binding.editRackViewModel = editRackViewModel
        editRackViewModel.rackId = args.rackID

        binding.btnDeleteRack.setOnClickListener{
            editRackViewModel.onDeleteRack()
            findNavController().navigate(R.id.action_editRackFragment_to_warehouseFragment)
        }
        binding.btnCancel.setOnClickListener{
            val action = EditRackFragmentDirections.actionEditRackFragmentToRackDetailsFragment(editRackViewModel.rackId)
            findNavController().navigate(action)
        }
        binding.btnSave.setOnClickListener{
            val newRackId = binding.editingEditTextAisle.text.toString() + "-" + binding.editingEditTextUnit.text.toString()
            editRackViewModel.updateRack(editRackViewModel.rackId, newRackId)
            val action = EditRackFragmentDirections.actionEditRackFragmentToRackDetailsFragment(newRackId)
            findNavController().navigate(action)
        }
        return binding.root
    }

}