package com.education.notes.presentation.fragments.notes.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.notes.R
import com.education.notes.presentation.model.User
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var userList = emptyList<User>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.itemView.row_id.text = currentItem.id.toString()
        holder.itemView.row_first_name.text = currentItem.firstName.toString()
        holder.itemView.row_last_name.text = currentItem.lastName.toString()
        holder.itemView.row_age.text = currentItem.age.toString()
    }

    fun setData(user: List <User>){
        this.userList = user
        notifyDataSetChanged()
    }
}
