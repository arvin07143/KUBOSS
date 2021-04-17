package com.example.kuboss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.database.User

class ViewUserAdapter(): RecyclerView.Adapter<ViewUserAdapter.UserViewHolder>() {
    var dataset = listOf<User>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): UserViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.user_row,
                parent, false
            )
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataset[position]
        holder.name.text = user.name
        holder.email.text = user.email
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}