package com.education.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.education.notes.model.NotesModel

@Database(entities = [NotesModel::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object{
        @Volatile
        private var INSTANCE: NotesDatabase? = null
        private const val DATABASE_NAME = "notes_database"

        fun getDatabase(context: Context): NotesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
        }
    }
}