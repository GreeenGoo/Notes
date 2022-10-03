package com.education.notes.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.notes.data.entity.NotesEntity
import com.education.notes.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val readAllData = MutableLiveData<List<NotesEntity>>()

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.getAllNotes())
        }
    }

    fun addNote(note: NotesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun updateNote(note: NotesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: NotesEntity) {
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