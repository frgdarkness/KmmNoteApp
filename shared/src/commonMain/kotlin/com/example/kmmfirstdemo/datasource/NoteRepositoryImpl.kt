package com.example.kmmfirstdemo.datasource

import com.example.kmmfirstdemo.DatabaseDriverFactory
import com.example.kmmfirstdemo.sqldelight.AppDatabase
import note.Note

class NoteRepositoryImpl(databaseDriverFactory: DatabaseDriverFactory) : NoteRepository {

    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val noteQueries = database.noteQueries

    override suspend fun addNote(note: Note) {
        noteQueries.insertOrUpdateNote(
            id = null,
            lastEditTime = note.lastEditTime,
            title = note.title,
            content = note.content,
        )
    }

    override suspend fun modifyNote(note: Note) {
        noteQueries.insertOrUpdateNote(
            id = note.id,
            lastEditTime = note.lastEditTime,
            title = note.title,
            content = note.content
        )
    }

    override suspend fun getAllNote(): List<Note> {
        return noteQueries.getAllNote().executeAsList()
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteQueries.getNoteById(id).executeAsOneOrNull()
    }

    override suspend fun getLastNote(): Note? {
        return noteQueries.getLastNote().executeAsOneOrNull()
    }

    override suspend fun getNoteByContent(content: String): List<Note> {
        return noteQueries.getNotByContent(content).executeAsList()
    }

    override suspend fun deleteNote(id: Long) {
        noteQueries.deleleNote(id)
    }
}