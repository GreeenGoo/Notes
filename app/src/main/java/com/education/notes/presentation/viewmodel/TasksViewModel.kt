package com.education.notes.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.notes.data.entity.TasksEntity
import com.education.notes.data.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {
    val readAllData = MutableLiveData<List<TasksEntity>>()

    fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.getAllTasks())
        }
    }

    fun addTask(task: TasksEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun updateTask(task: TasksEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: TasksEntity){
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