package com.education.notes.presentation.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.education.notes.presentation.model.Notes

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNote(note: Notes)

    @Update
    fun updateNote(note: Notes)

    @Delete
    fun deleteNote (note: Notes)

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Notes>>
}