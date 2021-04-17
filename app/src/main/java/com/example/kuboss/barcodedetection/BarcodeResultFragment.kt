/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.kuboss.barcodedetection

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.camera.WorkflowModel
import com.example.kuboss.camera.WorkflowModel.WorkflowState
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.warehouse.RackDetailsViewModel
import com.example.kuboss.warehouse.RackDetailsViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
class BarcodeResultFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_bottom_sheet, viewGroup)
        val confirmButton: Button = view.findViewById(R.id.btnConfirm)

        val arguments = arguments
        val barcodeFieldList: ArrayList<BarcodeField> =
            if (arguments?.containsKey(ARG_BARCODE_FIELD_LIST) == true) {
                arguments.getParcelableArrayList(ARG_BARCODE_FIELD_LIST) ?: ArrayList()
            } else {
                Log.e(TAG, "No barcode field list passed in!")
                ArrayList()
            }
        val rackID: String =
            if (arguments?.containsKey("rackID") == true) {
                arguments.getString("rackID") ?: ""
            } else {
                Log.e(TAG, "No rack id passed in!")
                String()
            }

        val mode: Int =
            if (arguments?.containsKey("saveMode") == true) {
                arguments.getInt("saveMode")
            } else {
                Log.e(TAG, "No save mode passed in!")
                1
            }

        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = RackDetailsViewModelFactory(dataSource, application, rackID)
        val rackDetailsViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(RackDetailsViewModel::class.java)

        val rawData = barcodeFieldList[0].value
        val barcodeData = rawData.split(',')
        val scannedMat = Material(barcodeData[0], barcodeData[1], barcodeData[2], barcodeData[3].toInt(), rackID)
        var matExist = true

        rackDetailsViewModel.allMaterialID.observe(viewLifecycleOwner, Observer {
            matExist = it.contains(barcodeData[0])
        })

        var potentialRack = ""
        rackDetailsViewModel.findRackID(barcodeData[0]).observe(viewLifecycleOwner, Observer {
            potentialRack = it ?: ""
        })

        confirmButton.setOnClickListener {
            if (mode == 1) {
                val storeMaterialError = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Store Material Error")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                    }

                if (matExist) {
                    Log.e("Test", potentialRack.toString())
                    if (potentialRack != "") { //item in other rack
                        storeMaterialError
                            .setMessage("Material Currently at Rack $potentialRack,\n\nYou are currently at Rack $rackID")
                            .show()
                    } else {
                        rackDetailsViewModel.storeMaterial(barcodeData[0], rackID)
                            .observe(viewLifecycleOwner, Observer {
                                if (it != 0) {
                                    Toast.makeText(context, "Item Stored Successfully", Toast.LENGTH_SHORT).show()
                                    activity?.finish()
                                }
                            })
                    }
                } else {
                    storeMaterialError
                        .setMessage("Material Does Not Exist")
                        .show()
                }
            } else {
                val pickErrorDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Pick Material Error")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                if (matExist) {
                    when {
                        potentialRack == "" -> {
                            pickErrorDialog.setMessage("Material has no assigned rack")
                            pickErrorDialog.show()
                        }
                        potentialRack != rackID -> {
                            pickErrorDialog.setMessage("Material Currently at Rack $potentialRack\n\nYou are currently on $rackID")
                            pickErrorDialog.show()
                        }
                        else -> {
                            rackDetailsViewModel.pickMaterial(scannedMat).observe(viewLifecycleOwner, Observer {
                                if (it == 1) {
                                    Toast.makeText(context, "Item Picked Successfully", Toast.LENGTH_SHORT).show()
                                    activity?.finish()
                                }
                            })
                        }
                    }
                } else {
                    pickErrorDialog.setMessage("Material Does Not Exist").show()
                }
            }

        }
        view.findViewById<RecyclerView>(R.id.barcode_field_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = BarcodeFieldAdapter(barcodeFieldList)
        }
        return view
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            // Back to working state after the bottom sheet is dismissed.
            ViewModelProviders.of(it).get(WorkflowModel::class.java).setWorkflowState(WorkflowState.DETECTING)
        }
        super.onDismiss(dialogInterface)
    }

    companion object {

        private const val TAG = "BarcodeResultFragment"
        private const val ARG_BARCODE_FIELD_LIST = "arg_barcode_field_list"

        fun show(
            fragmentManager: FragmentManager,
            barcodeFieldArrayList: ArrayList<BarcodeField>,
            saveMode: Int,
            rackId: String?
        ) {
            val barcodeResultFragment = BarcodeResultFragment()
            barcodeResultFragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_BARCODE_FIELD_LIST, barcodeFieldArrayList)
                putInt("saveMode", saveMode)
                putString("rackID", rackId)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultFragment?)?.dismiss()
        }
    }
}
