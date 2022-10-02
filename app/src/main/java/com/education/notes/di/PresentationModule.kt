package com.education.notes.di

import com.education.notes.data.NotesDatabase
import com.education.notes.data.TasksDatabase
import com.education.notes.presentation.viewmodel.NotesViewModel
import com.education.notes.presentation.viewmodel.TasksViewModel
import com.education.notes.repository.NotesRepository
import com.education.notes.repository.TasksRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notesModule = module {

    single {
        NotesDatabase.getDatabase(context = get()).notesDao()
    }

    single {
        NotesRepository(notesDao = get())
    }

    viewModel {
        NotesViewModel(repository = get())
    }
}

val tasksModule = module {
    single {
        TasksDatabase.getDataBase(context = get()).tasksDao()
    }

    single {
        TasksRepository(tasksDao = get())
    }

    viewModel {
        TasksViewModel(repository = get())
    }
}