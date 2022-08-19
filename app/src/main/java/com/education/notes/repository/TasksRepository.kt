package com.education.notes.repository

import com.education.notes.data.TasksDao
import com.education.notes.model.TasksModel

class TasksRepository(private val tasksDao: TasksDao) {

    suspend fun getAllTasks() : List<TasksModel> = tasksDao.readAllData()

    suspend fun addTask(task: TasksModel){
        tasksDao.addTask(task)
    }

    suspend fun updateTask(task: TasksModel){
        tasksDao.updateTask(task)
    }

    suspend fun deleteTask(task: TasksModel){
        tasksDao.deleteTask(task)
    }

    suspend fun deleteAllTasks(){
        tasksDao.deleteAllTasks()
    }
}