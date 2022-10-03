package com.education.notes.di

import com.education.notes.data.db.NotesDatabase
import com.education.notes.presentation.viewmodel.NotesViewModel
import com.education.notes.presentation.viewmodel.TasksViewModel
import com.education.notes.data.repository.NotesRepository
import com.education.notes.data.repository.TasksRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single {
        NotesDatabase.newInstance(context = get())
    }
}

val notesModule = module {
    single {
        NotesRepository(notesDatabase = get())
    }

    viewModel {
        NotesViewModel(repository = get())
    }
}

val tasksModule = module {
    single {
        TasksRepository(notesDatabase = get())
    }

    viewModel {
        TasksViewModel(repository = get())
    }
}