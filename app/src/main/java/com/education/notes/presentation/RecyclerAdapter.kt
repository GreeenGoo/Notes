package com.education.notes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.education.notes.R
import com.education.notes.databinding.CardViewLayoutBinding

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var title =
        arrayOf("First Part", "Second Part", "Third Part", "Fourth Part", "Fifth part")
    private var details = arrayOf(
        "First description",
        "Second description",
        "Third description",
        "Fourth description",
        "Fifth description"
    )
    private var images = intArrayOf(
        R.drawable.just_for_example_icon,
        R.drawable.just_for_example_icon,
        R.drawable.just_for_example_icon,
        R.drawable.just_for_example_icon,
        R.drawable.just_for_example_icon
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemDetail.text = details[position]
        holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)

    }
}