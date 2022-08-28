package com.education.notes.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.education.notes.data.TasksDatabase
import com.education.notes.model.TasksModel

import com.education.notes.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData = MutableLiveData<List<TasksModel>>()
    private val repository: TasksRepository

    init {
        val taskDao = TasksDatabase.getDataBase(application.applicationContext).tasksDao()
        repository = TasksRepository(taskDao)
    }

    fun getAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.getAllTasks())
        }
    }

    fun addTask(task: TasksModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTask(task)
        }
    }

    fun updateTask(task: TasksModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: TasksModel){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTask(task)
        }
    }

    fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllTasks()
            readAllData.postValue(emptyList())
        }
    }

    companion object{
        const val BUNDLE_KEY = "currentTask"
    }
}