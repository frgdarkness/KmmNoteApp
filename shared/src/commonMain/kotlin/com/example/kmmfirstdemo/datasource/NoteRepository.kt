package com.example.kmmfirstdemo.datasource

import note.Note

interface NoteRepository {
    suspend fun addNote(note: Note)

    suspend fun modifyNote(note: Note)

    suspend fun getAllNote() : List<Note>

    suspend fun getNoteById(id: Long) : Note?

    suspend fun getLastNote() : Note?

    suspend fun getNoteByContent(content: String) : List<Note>

    suspend fun deleteNote(id: Long)
}