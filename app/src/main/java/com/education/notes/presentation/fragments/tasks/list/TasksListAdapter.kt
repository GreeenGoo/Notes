package com.education.notes.presentation.fragments.tasks.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.education.notes.R
import com.education.notes.model.TasksModel
import java.lang.ref.WeakReference
import kotlinx.android.synthetic.main.task_column.view.item_text

typealias OnItemClickListener = (position: Int, doing: String) -> Unit

class TasksListAdapter(private val onItemClickListener:  OnItemClickListener) :
    RecyclerView.Adapter<TasksListAdapter.ViewHolder>() {
    private var _tasksList = emptyList<TasksModel>()
    private val tasksList get() = _tasksList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val view = WeakReference(itemView)
        private var textViewDelete: TextView? = null
        var index = 0

        var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

        init {
            view.get()?.let {

                it.setOnClickListener {
                    if (view.get()?.scrollX != 0)
                        updateView()
                }
                textViewDelete = it.findViewById(R.id.delete_text_view)
                val textDeleteViewVal = textViewDelete
                textDeleteViewVal?.setOnClickListener {
                    onDeleteClick?.let { onDeleteClick ->
                        onDeleteClick(this)
                    }
                }
            }
        }

        fun updateView() {
            view.get()?.scrollTo(0, 0)
        }
    }

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
        if (currentItem.crossed) {
            holder.itemView.item_text.text = TasksListFragment.toCrossLine(currentItem.text)
        } else {
            holder.itemView.item_text.text = currentItem.text
        }
        holder.itemView.item_text.setOnClickListener {
            onItemClickListener(position, "crossing")
        }

        holder.onDeleteClick = {
            onItemClickListener(position, "removing")
        }
    }

    fun setData (tasks: List<TasksModel>){
        this._tasksList = tasks
        notifyDataSetChanged()
    }

}

