package com.education.notes.presentation.fragments.notes.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.presentation.model.Notes
import kotlinx.android.synthetic.main.card_view_layout.view.*

class NotesListAdapter : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private var notesList = emptyList<Notes>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = notesList[position]
        holder.itemView.item_title.text = currentItem.title
        holder.itemView.item_description.text = currentItem.description
        Glide.with(holder.itemView.context).load(currentItem.imageURL)
            .into(holder.itemView.item_image)

        holder.itemView.card_view.setOnClickListener {
            val action =
                NotesListFragmentDirections.actionNavGraphListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(note: List <Notes>){
        this.notesList = note
        notifyDataSetChanged()
    }
}
