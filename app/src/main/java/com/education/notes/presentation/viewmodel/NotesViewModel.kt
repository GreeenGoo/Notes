package com.education.notes.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.notes.model.NotesModel
import com.education.notes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val readAllData = MutableLiveData<List<NotesModel>>()

    fun getAllNotes() {
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

    companion object{
        const val BUNDLE_KEY = "currentNote"
    }
}