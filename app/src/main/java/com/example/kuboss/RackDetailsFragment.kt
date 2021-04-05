package com.example.kuboss

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kuboss.adapter.RackItemAdapter
import com.example.kuboss.databinding.FragmentRackDetailsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RackDetailsFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentRackDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_rack_details, container, false)

        val application = requireNotNull(this.activity).application
        val adapter = RackItemAdapter()

        binding.lifecycleOwner = this
        binding.rackItemDetails.adapter = adapter
        adapter.dataset = listOf(Pair("Item A",1),Pair("Item B",2), Pair("Item C",69),Pair("Item D",420))

        val btnAdd:FloatingActionButton = binding.expandableFabLayout.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this.context,LiveBarcodeScanningActivity::class.java)
            intent.putExtra("mode",1)
            startActivity(intent)
        }

        val btnRemove:FloatingActionButton = binding.expandableFabLayout.findViewById(R.id.btnRemove)
        btnRemove.setOnClickListener {
            val intent = Intent(this.context,LiveBarcodeScanningActivity::class.java)
            intent.putExtra("mode",0)
            startActivity(intent)
        }
        return binding.root
    }
}