package com.example.kuboss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.database.Rack
import com.example.kuboss.warehouse.WarehouseFragmentDirections

class WarehouseRackAdapter:RecyclerView.Adapter<WarehouseRackAdapter.WarehouseRackViewHolder>() {
    var data = listOf<Rack>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class WarehouseRackViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val rackButton: Button = view.findViewById(R.id.rack_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseRackViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.rack_view, parent, false)
        return WarehouseRackViewHolder(layout)
    }

    override fun onBindViewHolder(holder: WarehouseRackViewHolder, position: Int) {
        val item = data[position]
        holder.rackButton.text = item.rackId
        holder.rackButton.setOnClickListener {
            val action = WarehouseFragmentDirections.actionWarehouseFragmentToRackDetailsFragment(item.rackId)
            holder.view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}