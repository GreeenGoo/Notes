package com.education.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.education.notes.model.TasksModel

@Database(entities = [TasksModel::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null
        private const val DATABASE_NAME = "tasks_database"

        fun getDataBase(context: Context): TasksDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TasksDatabase::class.java,
                DATABASE_NAME
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}