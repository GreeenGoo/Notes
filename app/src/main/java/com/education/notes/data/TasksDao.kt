package com.education.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.education.notes.model.TasksModel

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: TasksModel)

    @Update
    suspend fun updateTask(task: TasksModel)

    @Delete
    suspend fun deleteTask (task: TasksModel)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    suspend fun readAllData(): List<TasksModel>
}