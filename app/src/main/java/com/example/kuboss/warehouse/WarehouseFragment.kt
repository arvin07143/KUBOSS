package com.example.kuboss.warehouse

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kuboss.LiveBarcodeScanningActivity
import com.example.kuboss.R
import com.example.kuboss.adapter.WarehouseRackAdapter
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentWarehouseBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.util.*

class WarehouseFragment : Fragment() {
    private lateinit var warehouseViewModel: WarehouseViewModel
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
        warehouseViewModel = ViewModelProvider(
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
        val btnReport: FloatingActionButton = binding.expandableFabLayoutHome.findViewById(R.id.generate_report_btn)
        btnReport.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Generate Report")
                .setMessage("Are you sure that you want to generate material report?")
                .setCancelable(true)
                .setNegativeButton("Cancel"){ _,_ ->

                }
                .setPositiveButton("Ok") { _, _ ->
                    generateReport()
                }
                .show()
        }

        return binding.root
    }

    private fun generateReport(){
        val sdf = SimpleDateFormat.getDateTimeInstance()
        val currentDate = sdf.format(Date())
        val fileName = "Material Report-$currentDate.csv"
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, fileName)

        }

        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        val reportJob = GlobalScope.launch{
            val matList = warehouseViewModel.getReportString()
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                val os = requireContext().contentResolver.openOutputStream(uri!!)
                os?.write(matList.toByteArray())
                os?.close()

            }
        }
        reportJob.start()
    }


}