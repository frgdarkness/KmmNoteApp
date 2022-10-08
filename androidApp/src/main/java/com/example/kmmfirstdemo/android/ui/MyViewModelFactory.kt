package com.example.kmmfirstdemo.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kmmfirstdemo.datasource.NoteRepository

class MyViewModelFactory(private val repo: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NoteRepository::class.java).newInstance(repo)
    }
}