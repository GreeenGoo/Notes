package com.education.notes.presentation.repository

import androidx.lifecycle.LiveData
import com.education.notes.presentation.data.NotesDao
import com.education.notes.presentation.model.Notes

class NotesRepository(private val notesDao: NotesDao) {
    val readAllData: LiveData<List<Notes>> = notesDao.readAllData()

    fun addNote(note: Notes) {
        notesDao.addNote(note)
    }

    fun updateNote(note: Notes){
        notesDao.updateNote(note)
    }

    fun deleteNote (note: Notes){
        notesDao.deleteNote(note)
    }

    fun deleteAllNotes(){
        notesDao.deleteAllNotes()
    }
}