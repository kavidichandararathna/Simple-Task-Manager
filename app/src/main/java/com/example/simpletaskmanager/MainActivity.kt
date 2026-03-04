package com.example.simpletaskmanager

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletaskmanager.Model.TaskViewModel
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class Task(val id: Long, var title: String, var isCompleted: Boolean = false)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var taskList = mutableListOf<Task>()
    private lateinit var viewModel: TaskViewModel

    private val sharedPrefs by lazy { getSharedPreferences("TaskPrefs", MODE_PRIVATE) }
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTasks()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(
            taskList,
            onDeleteClick = { position ->
                taskList.removeAt(position)
                saveTasks()
                adapter.notifyItemRemoved(position)
            },
            onEditClick = { position ->
                showEditTaskDialog(position, taskList[position])
            },
            onCompleteToggle = { position ->
                taskList[position].isCompleted = !taskList[position].isCompleted
                saveTasks()
                adapter.notifyItemChanged(position)
            }


        )

        recyclerView.adapter = adapter

        val btnAdd: MaterialButton = findViewById(R.id.btnAddTask)
        btnAdd.setOnClickListener {
            showAddTaskDialog()
        }

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        viewModel.setTasks(taskList)

        viewModel.tasks.observe(this) {
            adapter.notifyDataSetChanged()
        }
    }

    //  Add Task
    private fun showAddTaskDialog() {
        val input = EditText(this)
        input.hint = "Enter task here"

        AlertDialog.Builder(this)
            .setTitle("Add New Task")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val taskTitle = input.text.toString()
        // Secure Coding Practice 1:
        // Input validation is performed to prevent saving empty tasks.
                if (taskTitle.isNotBlank()) {
                    val newTask = Task(System.currentTimeMillis(), taskTitle)
                    taskList.add(newTask)
                    saveTasks()
                    adapter.notifyItemInserted(taskList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Edit Task
    private fun showEditTaskDialog(position: Int, task: Task) {
        val input = EditText(this)
        input.setText(task.title)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = input.text.toString()

                if (updatedTitle.isNotBlank()) {
                    taskList[position].title = updatedTitle
                    saveTasks()
                    adapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    //  Secure Coding Practice 2:
        // Tasks are stored locally using SharedPreferences.
        // No sensitive data is hardcoded in the application.
        // This ensures user data is kept locally and not exposed externally.
    private fun saveTasks() {
        val json = gson.toJson(taskList)
        sharedPrefs.edit().putString("tasks_list", json).apply()
    }

    // Load Data
    private fun loadTasks() {
        val json = sharedPrefs.getString("tasks_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            taskList = gson.fromJson(json, type)
        }
    }
}