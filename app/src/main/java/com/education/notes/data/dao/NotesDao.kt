package com.education.notes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.education.notes.data.entity.NotesEntity

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: NotesEntity)

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Delete
    suspend fun deleteNote (note: NotesEntity)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    suspend fun readAllData(): List<NotesEntity>
}