package com.education.notes.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.notes.model.TasksModel
import com.education.notes.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {
    val readAllData = MutableLiveData<List<TasksModel>>()

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.getAllTasks())
        }
    }

    fun addTask(task: TasksModel) {
        viewModelScope.launch(Dispatchers.IO) {
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
}