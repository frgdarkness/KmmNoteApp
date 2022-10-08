package com.example.kmmfirstdemo.android.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmmfirstdemo.android.util.Constant.APPLICATION_TAG
import com.example.kmmfirstdemo.android.util.Constant.EMPTY_STRING
import com.example.kmmfirstdemo.android.util.DateTimeUtil
import com.example.kmmfirstdemo.datasource.NoteRepository
import kotlinx.coroutines.launch
import note.Note
import java.util.*

class DetailViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private val _note: MutableLiveData<Note> = MutableLiveData<Note>()
    val note: LiveData<Note> get() = _note

    private val _undoAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val undoAvailable: LiveData<Boolean> get() = _undoAvailable

    private val _redoAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val redoAvailable: LiveData<Boolean> get() = _redoAvailable

    private var isDeletedNote = false
    private var currentContent = EMPTY_STRING
    private val undoData = Stack<String>()
    private val redoData = Stack<String>()

    private fun resetData() {
        isDeletedNote = false
        currentContent = EMPTY_STRING
        undoData.clear()
        redoData.clear()
    }

    fun loadNote(id: Long) {
        Log.d(APPLICATION_TAG, "loadNote")
        resetData()
        viewModelScope.launch {
            _note.value = noteRepository.getNoteById(id)
            currentContent = note.value?.content ?: EMPTY_STRING
        }
    }

    fun createNote(title: String? = null, content: String? = null) {
        Log.d(APPLICATION_TAG, "createNote")
        viewModelScope.launch {
            noteRepository.addNote(
                Note(
                    id = 0,
                    lastEditTime = DateTimeUtil.getCurrentDateTime(),
                    title = title,
                    content = content
                )
            )
            _note.value = noteRepository.getLastNote()
        }
    }

    fun updateNote(newTitle: String?, newContent: String?) {
        Log.d(APPLICATION_TAG, "updateNote")
        if (isDeletedNote) return
        viewModelScope.launch {
            note.value?.copy(
                title = newTitle,
                content = newContent,
                lastEditTime = DateTimeUtil.getCurrentDateTime()
            )?.let { updatedNote ->
                _note.value = updatedNote
                noteRepository.modifyNote(updatedNote)
            }
        }
    }

    fun deleteNote() {
        Log.d(APPLICATION_TAG, "deleteNote")
        viewModelScope.launch {
            note.value?.id?.let { noteId ->
                Log.d(APPLICATION_TAG, "deleteNote: id = $noteId")
                noteRepository.deleteNote(noteId)
            }
            isDeletedNote = true
        }
    }


    fun updateContent(content: String) {
        Log.d(APPLICATION_TAG, "updateContent: $currentContent -> $content")
        Log.d(APPLICATION_TAG, "oldUndo = $undoData")
        if (content.length != currentContent.length) {
            undoData.push(currentContent)
            currentContent = content
            redoData.clear()
        }
        Log.d(APPLICATION_TAG, "newUndo = $undoData")
        Log.d(APPLICATION_TAG, "newRedo = $redoData")
        currentContent = content
        updateStateUndoRedo()
    }

    fun undo() {
        if (undoData.isEmpty()) return
        Log.d(APPLICATION_TAG, "do Undo")
        redoData.push(currentContent)
        currentContent = undoData.pop()
        note.value?.copy(
            content = currentContent,
            lastEditTime = DateTimeUtil.getCurrentDateTime()
        )?.let { updatedNote ->
            _note.value = updatedNote
        }
        updateStateUndoRedo()
    }

    fun redo() {
        if (redoData.isEmpty()) return
        Log.d(APPLICATION_TAG, "do Redo")
        undoData.push(currentContent)
        currentContent = redoData.pop()
        note.value?.copy(
            content = currentContent,
            lastEditTime = DateTimeUtil.getCurrentDateTime()
        )?.let { updatedNote ->
            _note.value = updatedNote
        }
        updateStateUndoRedo()
    }

    private fun updateStateUndoRedo() {
        _undoAvailable.value = !undoData.empty()
        _redoAvailable.value = !redoData.empty()
    }
}
