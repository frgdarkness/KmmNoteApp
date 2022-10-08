package com.example.kmmfirstdemo.android.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmmfirstdemo.android.util.Constant
import com.example.kmmfirstdemo.datasource.NoteRepository
import kotlinx.coroutines.launch
import note.Note

class HomeViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val noteListTemp = mutableListOf<Note>()
    private var _noteList = MutableLiveData<List<Note>>()
    val noteList: LiveData<List<Note>> get() = _noteList

    fun getAllNote() {
        Log.d(Constant.APPLICATION_TAG, "getAllNote")
        viewModelScope.launch {
            noteListTemp.clear()
            noteListTemp.addAll(repository.getAllNote())
            _noteList.value = noteListTemp
        }
    }

    fun searchNote(title: String?, content: String?) {
        Log.d(Constant.APPLICATION_TAG, "searchNote")
        val filteredNotes = noteListTemp.filter { note ->
            note.title.orEmpty().contains(title.orEmpty(), true) ||
                    note.content.orEmpty().contains(content.orEmpty(), true)
        }.distinct()
        _noteList.value = filteredNotes
    }
}