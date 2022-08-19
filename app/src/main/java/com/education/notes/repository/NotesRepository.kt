package com.education.notes.repository

import com.education.notes.data.NotesDao
import com.education.notes.model.NotesModel

class NotesRepository(private val notesDao: NotesDao) {

    suspend fun getAllNotes() : List<NotesModel> = notesDao.readAllData()


    suspend fun addNote(note: NotesModel) {
        notesDao.addNote(note)
    }

    suspend fun updateNote(note: NotesModel) {
        notesDao.updateNote(note)
    }

    suspend fun deleteNote(note: NotesModel) {
        notesDao.deleteNote(note)
    }

    suspend fun deleteAllNotes() {
        notesDao.deleteAllNotes()
    }
}