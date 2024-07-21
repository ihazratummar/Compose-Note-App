package com.example.mynotes.di

import android.content.Context
import androidx.room.Room
import com.example.mynotes.data.dao.NoteDao
import com.example.mynotes.data.database.NotesDatabase
import com.example.mynotes.data.repository.NoteRepositoryImpl
import com.example.mynotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.internal.notify
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context): NotesDatabase {

        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "note_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NotesDatabase): NoteDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun provideNoteRepository(db: NotesDatabase): NoteRepositoryImpl{
        return NoteRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}