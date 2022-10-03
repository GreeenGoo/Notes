package com.education.notes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.education.notes.data.dao.NotesDao
import com.education.notes.data.dao.TasksDao
import com.education.notes.data.entity.NotesEntity
import com.education.notes.data.entity.TasksEntity

private const val DB_VERSION = 1
private const val DB_NAME = "app.db"

@Database(entities = [NotesEntity::class, TasksEntity::class], version = DB_VERSION, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract val notesDao: NotesDao
    abstract val tasksDao: TasksDao

    companion object {
        fun newInstance(context: Context): NotesDatabase =
            Room.databaseBuilder(context, NotesDatabase::class.java, DB_NAME).build()
    }
}