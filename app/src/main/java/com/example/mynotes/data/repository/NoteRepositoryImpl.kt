package com.example.mynotes.data.repository

import com.example.mynotes.data.dao.NoteDao
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
): NoteRepository {
    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNotes(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNotes(note)
    }

    override fun getNotesByTitle(): Flow<List<Note>> {
        return  dao.getNotesByTitle()
    }

    override fun getNotesByDateAdded(): Flow<List<Note>> {
        return  dao.getNotesByDateAdded()
    }

    override fun getAllNotes(): List<Note> {
        return dao.getAllNotes()
    }

    override fun getNote(id: Int): Flow<Note> {
        return dao.getNote(id)
    }

    override fun getBookmarkedNotes(): Flow<List<Note>> {
        return dao.getBookmarkedNote()
    }

    override fun getNotesBySearchQuery(query: String): Flow<List<Note>> {
        return if (query.isBlank()) {
            // Return empty list if search query is blank
            flow { emit(emptyList()) }
        } else {
            val trimmedQuery = query.trim()
            dao.searchNotes(trimmedQuery)
        }
    }
}