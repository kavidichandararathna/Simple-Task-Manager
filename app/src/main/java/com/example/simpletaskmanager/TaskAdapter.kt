package com.example.simpletaskmanager

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView




class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onCompleteToggle: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkCompleted)


        val textView: TextView = view.findViewById(R.id.tvTaskTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.textView.text = task.title
        holder.checkBox.isChecked = task.isCompleted

        if (task.isCompleted) {
            holder.textView.paintFlags =
                holder.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.textView.paintFlags =
                holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkBox.setOnClickListener {
            onCompleteToggle(position)
        }

        holder.itemView.setOnClickListener {
            onEditClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onDeleteClick(position)
            true
        }
    }

    override fun getItemCount() = tasks.size
}