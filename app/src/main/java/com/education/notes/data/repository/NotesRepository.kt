package com.education.notes.data.repository

import com.education.notes.data.db.NotesDatabase
import com.education.notes.data.entity.NotesEntity

class NotesRepository(private val notesDatabase: NotesDatabase) {

    suspend fun getAllNotes() : List<NotesEntity> = notesDatabase.notesDao.readAllData()


    suspend fun addNote(note: NotesEntity) {
        notesDatabase.notesDao.addNote(note)
    }

    suspend fun updateNote(note: NotesEntity) {
        notesDatabase.notesDao.updateNote(note)
    }

    suspend fun deleteNote(note: NotesEntity) {
        notesDatabase.notesDao.deleteNote(note)
    }

    suspend fun deleteAllNotes() {
        notesDatabase.notesDao.deleteAllNotes()
    }
}