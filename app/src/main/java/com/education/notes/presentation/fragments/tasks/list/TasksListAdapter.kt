package com.education.notes.presentation.fragments.tasks.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.notes.R
import com.education.notes.data.entity.TasksEntity
import kotlinx.android.synthetic.main.task_column.view.item_text

typealias OnItemClickListener = (position: Int) -> Unit

class TasksListAdapter(private val onItemClickListener:  OnItemClickListener) :
    RecyclerView.Adapter<TasksListAdapter.ViewHolder>() {
    private var tasksList = emptyList<TasksEntity>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.task_column,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = tasksList[position]
        if (currentItem.crossed) {
            holder.itemView.item_text.text = TasksListFragment.toCrossLine(currentItem.text)
        } else {
            holder.itemView.item_text.text = currentItem.text
        }
        holder.itemView.item_text.setOnClickListener {
            onItemClickListener(position)
        }
    }

    fun setData (tasks: List<TasksEntity>){
        this.tasksList = tasks
        notifyDataSetChanged()
    }
}

