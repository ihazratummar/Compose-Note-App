package com.example.mynotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mynotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNotes(notes: Note)

    @Delete
    suspend fun deleteNotes(notes: Note)

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateAdded DESC")
    fun getNotesByDateAdded(): Flow<List<Note>>

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :id" )
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * FROM note WHERE isBookmarked = 1")
    fun getBookmarkedNote() : Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

}