package com.example.dailytaskmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

data class Task(var title: String, var isCompleted: Boolean)

class TaskAdapter(
    private val tasks: List<Task>,
    private val onChecked: (Task) -> Unit,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskTitle: TextView = view.findViewById(R.id.task_title)
        val taskCheckbox: CheckBox = view.findViewById(R.id.task_checkbox)
        val editButton: TextView = view.findViewById(R.id.edit_button)
        val deleteButton: TextView = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTitle.text = task.title
        holder.taskCheckbox.isChecked = task.isCompleted

        holder.taskCheckbox.setOnCheckedChangeListener { _, _ ->
            onChecked(task)
        }

        holder.editButton.setOnClickListener {
            onEdit(task)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount() = tasks.size
}