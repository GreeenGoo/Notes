package com.education.notes.data.repository

import com.education.notes.data.db.NotesDatabase
import com.education.notes.data.entity.TasksEntity

class TasksRepository(private val notesDatabase: NotesDatabase) {

    suspend fun getAllTasks() : List<TasksEntity> = notesDatabase.tasksDao.readAllData()

    suspend fun addTask(task: TasksEntity){
        notesDatabase.tasksDao.addTask(task)
    }

    suspend fun updateTask(task: TasksEntity){
        notesDatabase.tasksDao.updateTask(task)
    }

    suspend fun deleteTask(task: TasksEntity){
        notesDatabase.tasksDao.deleteTask(task)
    }

    suspend fun deleteAllTasks(){
        notesDatabase.tasksDao.deleteAllTasks()
    }
}