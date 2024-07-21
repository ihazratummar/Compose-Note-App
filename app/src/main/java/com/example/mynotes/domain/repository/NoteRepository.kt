package com.example.mynotes.domain.repository

import com.example.mynotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNotesByTitle(): Flow<List<Note>>
    fun getNotesByDateAdded(): Flow<List<Note>>
    fun getAllNotes(): List<Note>
    fun getNote(id: Int): Flow<Note>
    fun getBookmarkedNotes(): Flow<List<Note>>
    fun getNotesBySearchQuery(query: String): Flow<List<Note>>
}