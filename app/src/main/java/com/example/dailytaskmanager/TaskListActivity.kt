package com.example.dailytaskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class TaskListActivity : AppCompatActivity() {

    private val filePath by lazy { File(filesDir, "tasks.json") }
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var backButton: Button
    lateinit var completionPercentageTextView: TextView
    private lateinit var noTasksMessage: TextView
    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskRecyclerView = findViewById(R.id.task_recycler_view)
        backButton = findViewById(R.id.back_button)
        completionPercentageTextView = findViewById(R.id.completion_percentage_text_view)
        noTasksMessage = findViewById(R.id.no_tasks_message)

        loadTasks()
        setupRecyclerView()

        backButton.setOnClickListener {
            // Calculate completion percentage and return it to MainActivity
            val completionPercentage = calculateCompletionPercentage(tasks)
            val resultIntent = Intent()
            resultIntent.putExtra("completionPercentage", completionPercentage)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        updateCompletionPercentage() // Update completion percentage on screen
    }

    private fun loadTasks() {
        if (filePath.exists()) {
            val json = filePath.readText()
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                tasks.add(Task(obj.getString("title"), obj.getBoolean("isCompleted")))
            }
        }
    }

    private fun saveTasks() {
        val jsonArray = JSONArray()
        tasks.forEach {
            val obj = JSONObject()
            obj.put("title", it.title)
            obj.put("isCompleted", it.isCompleted)
            jsonArray.put(obj)
        }
        filePath.writeText(jsonArray.toString())
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(tasks, ::onTaskChecked, ::onTaskEdit, ::onTaskDelete)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.adapter = taskAdapter
        updateTaskListVisibility()
    }

    private fun onTaskChecked(task: Task) {
        task.isCompleted = !task.isCompleted
        saveTasks()
        taskAdapter.notifyDataSetChanged()
        updateCompletionPercentage()
    }

    private fun onTaskEdit(task: Task) {
        // Create a dialog for editing the task title
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Edit Task")

        // Add an EditText to the dialog
        val input = android.widget.EditText(this)
        input.setText(task.title) // Pre-fill the current title
        builder.setView(input)

        // Set up dialog buttons
        builder.setPositiveButton("Save") { _, _ ->
            val newTitle = input.text.toString().trim()
            if (newTitle.isNotEmpty()) {
                task.title = newTitle // Update the task title
                saveTasks() // Save changes to the file
                taskAdapter.notifyDataSetChanged() // Refresh RecyclerView
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel() // Close dialog
        }

        // Show the dialog
        builder.show()
    }

    private fun onTaskDelete(task: Task) {
        tasks.remove(task)
        saveTasks()
        taskAdapter.notifyDataSetChanged()
        updateCompletionPercentage()
    }

    private fun updateCompletionPercentage() {
        val completionPercentage = calculateCompletionPercentage(tasks)
        completionPercentageTextView.text = "Completion: $completionPercentage%"
    }

    private fun calculateCompletionPercentage(tasks: List<Task>): Int {
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isCompleted }
        return if (totalTasks == 0) 0 else (completedTasks * 100) / totalTasks
    }

    // Method to handle showing/hiding the "No Tasks" message
    private fun updateTaskListVisibility() {
        if (tasks.isEmpty()) {
            taskRecyclerView.visibility = RecyclerView.GONE
            noTasksMessage.visibility = TextView.VISIBLE
        } else {
            taskRecyclerView.visibility = RecyclerView.VISIBLE
            noTasksMessage.visibility = TextView.GONE
        }
    }
}
