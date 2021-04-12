package com.example.kuboss.warehouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kuboss.LiveBarcodeScanningActivity
import com.example.kuboss.R
import com.example.kuboss.adapter.RackItemAdapter
import com.example.kuboss.database.Material
import com.example.kuboss.database.RackWithMaterials
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentRackDetailsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RackDetailsFragment : Fragment(){
    var count = 1
    val args: RackDetailsFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //inflate binding
        val binding: FragmentRackDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_rack_details, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = RackDetailsViewModelFactory(dataSource, application, args.rackID)
        val rackDetailsViewModel = ViewModelProvider(
            this, viewModelFactory).get(RackDetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.rackDetailsViewModel = rackDetailsViewModel
        Log.d("rackid", rackDetailsViewModel.rackId)
        //setup material's adapter
        val adapter = RackItemAdapter()
        binding.rackItemDetails.adapter = adapter
        rackDetailsViewModel.materialList.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.dataset = it.materials
            }
        })

        //setup floating action button
        val btnAdd:FloatingActionButton = binding.expandableFabLayout.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this.context, LiveBarcodeScanningActivity::class.java)
            intent.putExtra("mode",1)
            intent.putExtra("rackID",args.rackID)
            Log.e("Test",args.rackID)
            startActivity(intent)
//            val mat = Material(count.toString(), "sku", "cola", 1, rackDetailsViewModel.rackId)
//            rackDetailsViewModel.storeMaterial(mat)
//            count += 1
        }

        val btnRemove:FloatingActionButton = binding.expandableFabLayout.findViewById(R.id.btnRemove)
        btnRemove.setOnClickListener {
            val intent = Intent(this.context, LiveBarcodeScanningActivity::class.java)
            intent.putExtra("mode",0)
            intent.putExtra("rackID",args.rackID)
            startActivity(intent)
        }

        val btnEdit:FloatingActionButton = binding.expandableFabLayout.findViewById(R.id.btnEdit)
        btnEdit.setOnClickListener{
            val action = RackDetailsFragmentDirections.actionRackDetailsFragmentToEditRackFragment(args.rackID)
            findNavController().navigate(action)
        }


        return binding.root
    }
}