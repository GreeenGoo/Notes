package com.education.notes.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.education.notes.data.NotesDatabase
import com.education.notes.model.NotesModel
import com.education.notes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {

    val readAllData = MutableLiveData<List<NotesModel>>()
    private val repository: NotesRepository

    init {
        val notesDao = NotesDatabase.getDatabase(application.applicationContext).notesDao()
        repository = NotesRepository(notesDao)
    }

    fun getAllNotes(){
         viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.getAllNotes())
        }
    }

    fun addNote(note: NotesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun updateNote(note: NotesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: NotesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
            readAllData.postValue(emptyList())
        }
    }
}