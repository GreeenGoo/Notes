package com.education.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.education.notes.model.NotesModel

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: NotesModel)

    @Update
    suspend fun updateNote(note: NotesModel)

    @Delete
    suspend fun deleteNote (note: NotesModel)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    suspend fun readAllData(): List<NotesModel>
}