package com.example.noteapp.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.model.NoteData

// the function of view model factory if you want to pass any data to view model ..

class DetailScreenViewModelFactory(private val noteData: NoteData): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(noteData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}