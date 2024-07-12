package com.example.mynotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynotes.data.dao.NoteDao
import com.example.mynotes.domain.model.Note

@Database(entities = [Note::class], version = 3)
abstract class NotesDatabase: RoomDatabase() {
    abstract val dao: NoteDao
}