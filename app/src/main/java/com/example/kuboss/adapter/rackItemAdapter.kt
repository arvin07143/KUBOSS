package com.example.kuboss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.database.Material
import com.example.kuboss.database.RackWithMaterials

class RackItemAdapter(): RecyclerView.Adapter<RackItemAdapter.ItemViewHolder>() {
    var dataset = listOf<Material>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemNameView: TextView = itemView.findViewById(R.id.item_title)
        val itemCountView: TextView = itemView.findViewById(R.id.item_amount)
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
        holder.itemNameView.text = item.materialId
        holder.itemCountView.text = item.mRackId
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}