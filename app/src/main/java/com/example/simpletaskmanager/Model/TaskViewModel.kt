package com.example.simpletaskmanager.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpletaskmanager.Task

class TaskViewModel : ViewModel() {

    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> = _tasks

    fun setTasks(list: MutableList<Task>) {
        _tasks.value = list
    }

    fun addTask(task: Task) {
        _tasks.value?.add(task)
        _tasks.value = _tasks.value
    }

    fun updateTask(position: Int, title: String) {
        _tasks.value?.get(position)?.title = title
        _tasks.value = _tasks.value
    }

    fun deleteTask(position: Int) {
        _tasks.value?.removeAt(position)
        _tasks.value = _tasks.value
    }
}