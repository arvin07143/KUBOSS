package com.example.kuboss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kuboss.R
import com.example.kuboss.database.User

class deleteUserAdapter:RecyclerView.Adapter<deleteUserAdapter.DeleteUserViewHolder>(){
    var selectedUserForDelete:MutableList<String> = arrayListOf()

    var dataset = listOf<User>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    class DeleteUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
        val userSelected: CheckBox = itemView.findViewById(R.id.cbUserSelected)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DeleteUserViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.delete_user_row,
                parent, false
            )
        return DeleteUserViewHolder(view)

    }

    override fun onBindViewHolder(holder: DeleteUserViewHolder, position: Int) {
        val user = dataset[position]
        holder.name.text = user.name
        holder.email.text = user.email
        holder.userSelected.text = user.name
        holder.userSelected.setOnClickListener{
            selectedUserForDelete.add(user.name)
        }
    }

    fun returnUserForDelete(): MutableList<String>{
        return selectedUserForDelete

    }


    override fun getItemCount(): Int {
        return dataset.size
    }

}