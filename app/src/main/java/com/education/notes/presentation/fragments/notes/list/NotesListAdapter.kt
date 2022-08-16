package com.education.notes.presentation.fragments.notes.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.model.NotesModel
import kotlinx.android.synthetic.main.card_view_layout.view.card_view
import kotlinx.android.synthetic.main.card_view_layout.view.item_description
import kotlinx.android.synthetic.main.card_view_layout.view.item_image
import kotlinx.android.synthetic.main.card_view_layout.view.item_title

typealias OnItemClickListener = (position: Int) -> Unit

class NotesListAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private var _notesList = emptyList<NotesModel>()
    private val noteList get() = _notesList

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
