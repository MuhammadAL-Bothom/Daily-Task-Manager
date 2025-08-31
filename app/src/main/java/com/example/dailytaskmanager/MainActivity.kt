package com.example.dailytaskmanager

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var viewTasksButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var completionPercentageTextView: TextView

    private val filePath by lazy { File(filesDir, "tasks.json") }

    // Activity Result Launcher for both TaskList and AddTask activities
    private val getResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh completion percentage when a new task is added or other activities return
                updateCompletionPercentage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timer_text_view)
        viewTasksButton = findViewById(R.id.view_tasks_button)
        addTaskButton = findViewById(R.id.add_task_button)
        completionPercentageTextView = findViewById(R.id.completion_percentage_text_view)

        startCountdownTimer()
        updateCompletionPercentage()

        viewTasksButton.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            getResult.launch(intent) // Launch TaskListActivity and wait for result
        }

        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            getResult.launch(intent) // Launch AddTaskActivity and wait for result
        }
    }

    private fun startCountdownTimer() {
        val countdownDuration = 1 * 60 * 1000L // 1 minute in milliseconds
        val restDuration = 30 * 1000L // 30 seconds for rest

        object : CountDownTimer(countdownDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (1000 * 60)
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "Rest Time"
                addTaskButton.isClickable = false;
                addTaskButton.setBackgroundColor(getColor(R.color.gray))

                object : CountDownTimer(restDuration, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timerTextView.text = "Rest: ${millisUntilFinished / 1000}s"
                    }

                    override fun onFinish() {
                        startCountdownTimer()
                        deleteAllTasks()
                        addTaskButton.isClickable = true;
                        addTaskButton.setBackgroundColor(getColor(R.color.buttonColor))
                    }
                }.start()
            }
        }.start()
    }

    private fun deleteAllTasks() {
        if (filePath.exists()) {
            filePath.delete()
        }
        updateCompletionPercentage()
    }

    private fun updateCompletionPercentage() {
        val tasks = loadTasks()
        val completionPercentage = calculateCompletionPercentage(tasks)
        completionPercentageTextView.text = "Completion: $completionPercentage%"
    }

    private fun loadTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        if (filePath.exists()) {
            val json = filePath.readText()
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                tasks.add(Task(obj.getString("title"), obj.getBoolean("isCompleted")))
            }
        }
        return tasks
    }

    private fun calculateCompletionPercentage(tasks: List<Task>): Int {
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.isCompleted }
        return if (totalTasks == 0) 0 else (completedTasks * 100) / totalTasks
    }
}
