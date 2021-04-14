package com.example.kuboss.warehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentEditRackBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        val viewModelFactory = RackDetailsViewModelFactory(dataSource, application, args.rackID)
        val editRackViewModel = ViewModelProvider(
            this, viewModelFactory).get(RackDetailsViewModel::class.java)

        binding.editRackViewModel = editRackViewModel
        Log.d("editrackid", editRackViewModel.rackId)
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
            if(newRackId != editRackViewModel.rackId ) {
                editRackViewModel.updateRack(editRackViewModel.rackId, newRackId)
                val action =
                    EditRackFragmentDirections.actionEditRackFragmentToRackDetailsFragment(newRackId)
                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(), "Rack already exist", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

}