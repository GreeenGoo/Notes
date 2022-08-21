package com.education.notes.presentation.fragments.tasks.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.notes.R
import com.education.notes.model.TasksModel
import kotlinx.android.synthetic.main.task_column.view.item_text
import kotlinx.android.synthetic.main.task_column.view.task_column

typealias OnItemClickListener = (position: Int, holder: TasksListAdapter.ViewHolder) -> Unit

class TasksListAdapter(private val onItemClickListener:  OnItemClickListener) :
    RecyclerView.Adapter<TasksListAdapter.ViewHolder>() {
    private var _tasksList = emptyList<TasksModel>()
    private val tasksList get() = _tasksList

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
        val currentItem = _tasksList[position]
        /*if (currentItem.crossed){

        }
        else{*/
            holder.itemView.item_text.text = currentItem.text
       /* }*/
        holder.itemView.task_column.setOnClickListener{
            onItemClickListener(position, holder)
        }
    }

    fun setData (tasks: List<TasksModel>){
        this._tasksList = tasks
        notifyDataSetChanged()
    }
}

