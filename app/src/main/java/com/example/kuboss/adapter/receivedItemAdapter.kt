package com.example.kuboss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.database.Material

class ReceivedItemAdapter(): RecyclerView.Adapter<ReceivedItemAdapter.ItemViewHolder>() {
    var dataset = listOf<Material>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemIdView: TextView = itemView.findViewById(R.id.material_id)
        val itemNameView: TextView = itemView.findViewById(R.id.material_name)
        val itemSkuView: TextView = itemView.findViewById(R.id.material_SKU)
        val itemQtyView: TextView = itemView.findViewById(R.id.material_qty)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.list_item,
                parent, false
            )
        return ItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.itemIdView.text = item.materialId
        holder.itemNameView.text = item.materialName
        holder.itemSkuView.text = item.SKU
        holder.itemQtyView.text = item.quantity.toString()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}