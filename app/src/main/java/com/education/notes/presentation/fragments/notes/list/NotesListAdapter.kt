package com.education.notes.presentation.fragments.notes.list

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.model.NotesModel
import com.google.android.material.color.MaterialColors.getColor
import kotlinx.android.synthetic.main.card_view_layout.view.*

typealias OnItemClickListener = (position: Int) -> Unit

class NotesListAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private var _notesList = emptyList<NotesModel>()
    val noteList get() = _notesList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = _notesList[position]
        holder.itemView.item_title.text = currentItem.title
        holder.itemView.item_description.text = currentItem.description
        Glide.with(holder.itemView.context).load(Uri.parse(currentItem.imageURL))
            .into(holder.itemView.item_image)
        holder.itemView.card_view.setOnClickListener {
            onItemClickListener(position)
        }
    }

    fun setData(note: List<NotesModel>) {
        this._notesList = note
        notifyDataSetChanged()
    }
}
