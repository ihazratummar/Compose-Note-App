package com.example.mynotes.di

import com.example.mynotes.data.repository.NoteRepositoryImpl
import com.example.mynotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindWeatherRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}