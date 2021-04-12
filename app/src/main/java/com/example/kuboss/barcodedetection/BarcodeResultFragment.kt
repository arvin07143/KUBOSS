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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.kuboss.camera.WorkflowModel
import com.example.kuboss.camera.WorkflowModel.WorkflowState
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.warehouse.RackDetailsViewModel
import com.example.kuboss.warehouse.RackDetailsViewModelFactory

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
class BarcodeResultFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_bottom_sheet, viewGroup)
        val confirmButton : Button = view.findViewById(R.id.btnConfirm)

        val arguments = arguments
        val barcodeFieldList: ArrayList<BarcodeField> =
            if (arguments?.containsKey(ARG_BARCODE_FIELD_LIST) == true) {
                arguments.getParcelableArrayList(ARG_BARCODE_FIELD_LIST) ?: ArrayList()
            } else {
                Log.e(TAG, "No barcode field list passed in!")
                ArrayList()
            }
        val rackID: String =
            if (arguments?.containsKey("rackID") == true){
                arguments.getString("rackID") ?: ""
            } else{
                Log.e(TAG,"No rack id passed in!")
                String()
            }

        val mode: Int =
            if(arguments?.containsKey("saveMode") == true){
                arguments.getInt("saveMode") ?: 1
            } else{
                Log.e(TAG,"No save mode passed in!")
                1
            }

        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = RackDetailsViewModelFactory(dataSource, application,rackID)
        val rackDetailsViewModel = ViewModelProvider(
            this, viewModelFactory).get(RackDetailsViewModel::class.java)

        confirmButton.setOnClickListener {
            if (mode == 1){
                val rawData = barcodeFieldList[0].value
                val barcodeData = rawData.split(',')
                Log.e("test",barcodeData[0])
                Log.e("test",barcodeData[1])
                Log.e("test",barcodeData[2])
                Log.e("test",barcodeData[3])
                Log.e("test",rackID)
                val addMat = Material(barcodeData[0],barcodeData[1],barcodeData[2],barcodeData[3].toInt(),rackID)
                rackDetailsViewModel.storeMaterial(addMat)
                activity?.finish()
            } else {
                // minus
                activity?.finish()
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
                putInt("saveMode",saveMode)
                putString("rackID",rackId)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultFragment?)?.dismiss()
        }
    }
}
