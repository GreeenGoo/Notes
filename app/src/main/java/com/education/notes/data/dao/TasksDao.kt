package com.education.notes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.education.notes.data.entity.TasksEntity

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: TasksEntity)

    @Update
    suspend fun updateTask(task: TasksEntity)

    @Delete
    suspend fun deleteTask (task: TasksEntity)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    suspend fun readAllData(): List<TasksEntity>
}