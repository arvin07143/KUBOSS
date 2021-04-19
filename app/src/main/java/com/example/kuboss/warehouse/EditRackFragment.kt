package com.example.kuboss.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentEditRackBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_rack, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = RackDetailsViewModelFactory(dataSource, application, args.rackID)
        val editRackViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(RackDetailsViewModel::class.java)
        binding.editRackViewModel = editRackViewModel
        editRackViewModel.getRackList()
        binding.btnDeleteRack.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Warning")
                .setMessage("Are you sure that you want to remove this rack?\nThe material stored will be transferred to received list.")
                .setCancelable(true)
                .setNegativeButton("Cancel") { _, _ -> }
                .setPositiveButton("Ok") { _, _ ->
                    editRackViewModel.onDeleteRack()
                    findNavController().navigate(R.id.action_editRackFragment_to_warehouseFragment)
                }
                .show()

        }
        binding.btnCancel.setOnClickListener {
            val action = EditRackFragmentDirections.actionEditRackFragmentToRackDetailsFragment(
                editRackViewModel.rackId
            )
            findNavController().navigate(action)
        }
        binding.btnSave.setOnClickListener {
            val aisle = binding.editingEditTextAisle.text.toString()
            val unit = binding.editingEditTextUnit.text.toString()
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
            } else if (newRackId != editRackViewModel.rackId
                && !editRackViewModel.isRackExist(newRackId)
            ) {
                editRackViewModel.updateRack(editRackViewModel.rackId, newRackId)
                val action =
                    EditRackFragmentDirections.actionEditRackFragmentToRackDetailsFragment(
                        newRackId
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Rack already exist", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

}