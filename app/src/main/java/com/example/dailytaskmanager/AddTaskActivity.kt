package com.example.dailytaskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AddTaskActivity : AppCompatActivity() {

    private val filePath by lazy { File(filesDir, "tasks.json") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val taskInput: EditText = findViewById(R.id.task_input)
        val saveButton: Button = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val taskTitle = taskInput.text.toString()
            if (taskTitle.isNotEmpty()) {
                val tasks = mutableListOf<Task>()
                if (filePath.exists()) {
                    val json = filePath.readText()
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        tasks.add(Task(obj.getString("title"), obj.getBoolean("isCompleted")))
                    }
                }
                tasks.add(Task(taskTitle, false))
                val jsonArray = JSONArray()
                tasks.forEach {
                    val obj = JSONObject()
                    obj.put("title", it.title)
                    obj.put("isCompleted", it.isCompleted)
                    jsonArray.put(obj)
                }
                filePath.writeText(jsonArray.toString())
                Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show()

                // Notify MainActivity to refresh completion percentage
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Task cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun back(view: View) {
        finish()
    }
}
