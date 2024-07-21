package com.example.mynotes

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.NoteRepository
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.event.SortType
import com.example.mynotes.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _sortType = MutableStateFlow(PreferenceUtil.getSortType(context))
    private val _searchQuery = MutableStateFlow("")
    private val _state = MutableStateFlow(
        NoteState(
            isToggleView = PreferenceUtil.getToggleView(context),
            isDarkMode = PreferenceUtil.getThemeMode(context),
            currentLanguage = PreferenceUtil.getLanguage(context)
        )
    )
    private var searchJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _notes = combine(_sortType, _searchQuery) { sortType, query ->
        sortType to query
    }.flatMapLatest { (sortType, query) ->
        if (query.isBlank()) {
            when (sortType) {
                SortType.Title -> repository.getNotesByTitle()
                SortType.Date -> repository.getNotesByDateAdded()
            }
        } else {
            repository.getNotesBySearchQuery(query)
                .debounce(500L)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(_state, _sortType, _notes) { state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    init {
        refreshNotes()
        viewModelScope.launch {

        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.SaveNotes -> {
                val title = _state.value.title
                val description = _state.value.description
                val dateAdded = System.currentTimeMillis()

                val note = Note(
                    title = title ?: "New Note",
                    description = description ?: "Description",
                    dateAdded = dateAdded
                )
                viewModelScope.launch {
                    repository.insertNote(note)
                }
                _state.update {
                    it.copy(
                        isAddingNote = false,
                        title = null,
                        description = null,
                    )
                }
            }

            is NoteEvent.SetTitle -> {
                _state.update {
                    val newState = it.copy(
                        title = event.title
                    )
                    newState.copy(
                        isAddingFormValid = isFormValid(newState)
                    )
                }
            }

            is NoteEvent.SetDescription -> {
                _state.update {
                    val newState = it.copy(
                        description = event.description
                    )
                    newState.copy(
                        isAddingFormValid = isFormValid(newState)
                    )
                }
            }

            is NoteEvent.UpdateNote -> {
                val title = _state.value.updateTitle ?: event.note.title
                val description = _state.value.updateDescription ?: event.note.description

                val note = Note(
                    id = event.note.id,
                    title = title,
                    description = description,
                    dateAdded = event.note.dateAdded
                )
                viewModelScope.launch {
                    repository.updateNote(note)
                    refreshNotes()
                }
                _state.update {
                    it.copy(
                        isUpdatingNote = false,
                        updateTitle = null,
                        updateDescription = null,
                        updateColor = null,
                    )
                }
            }

            is NoteEvent.UpdateTitle -> {
                _state.update {
                    it.copy(
                        updateTitle = event.title
                    )
                }
            }

            is NoteEvent.UpdateDescription -> {
                _state.update {
                    it.copy(
                        updateDescription = event.description
                    )
                }
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
                PreferenceUtil.setSortType(context, event.sortType)
            }

            NoteEvent.ShowDeleteNoteDialog -> {
                _state.update {
                    it.copy(
                        isDeletingNote = true
                    )
                }
            }

            NoteEvent.HideDeleteNoteDialog -> {
                _state.update {
                    it.copy(
                        isDeletingNote = false
                    )
                }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteNote(event.note)
                    refreshNotes()
                    _state.update {
                        it.copy(
                            isDeletingNote = false
                        )
                    }
                }
            }

            NoteEvent.RefreshNotes -> {
                refreshNotes()
            }

            NoteEvent.ToggleSort -> {
                val newSortType =
                    if (_sortType.value == SortType.Date) SortType.Title else SortType.Date
                _sortType.value = newSortType
                PreferenceUtil.setSortType(context, newSortType)
            }

            is NoteEvent.BookmarkNote -> {
                viewModelScope.launch {
                    val updatedNote = event.note.copy(
                        isBookmarked = !event.note.isBookmarked
                    )
                    repository.updateNote(updatedNote)
                    refreshNotes()
                }
            }

            is NoteEvent.SetSearchQuery -> {
                Log.d("NoteViewModel", "Search query: ${event.query}")
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    _searchQuery.value = event.query
                    _state.update {
                        it.copy(
                            searchText = event.query
                        )
                    }
                }
            }

            NoteEvent.ToggleView -> {
                _state.update {
                    it.copy(
                        isToggleView = !it.isToggleView
                    )
                }
                PreferenceUtil.setToggleView(context, _state.value.isToggleView)
            }

            NoteEvent.ToggleTheme -> {
                toggleTheme()
            }

            is NoteEvent.ChangeLanguage -> {
                val newLanguage = event.language.takeIf { it.isNotBlank() }?:"en"
                _state.update {
                    it.copy(
                        currentLanguage = newLanguage
                    )
                }
                PreferenceUtil.setLanguage(context, newLanguage)
                applyLanguageChange()
            }
            NoteEvent.LanguageDialog ->{
                _state.update {
                    it.copy(
                        isLanguageDialogBoxOpen = !it.isLanguageDialogBoxOpen
                    )
                }
            }
        }
    }

    private fun applyLanguageChange() {
        viewModelScope.launch {
            if (context is Activity) {
                val intent = context.intent
                context.finish()
                context.startActivity(intent)
            }
        }
    }

    private fun toggleTheme() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isDarkMode = !it.isDarkMode
                )
            }
            PreferenceUtil.setThemeMode(context, _state.value.isDarkMode)
        }
    }

    private fun refreshNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val notes = repository.getAllNotes()
            _state.update {
                it.copy(
                    notes = notes,
                    isUpdatingNote = false,
                    isAddingNote = false,
                    isDeletingNote = false,
                    updateTitle = null,
                    updateDescription = null,
                    updateColor = null,
                )
            }
        }
    }

    private fun isFormValid(state: NoteState): Boolean {
        return !state.title.isNullOrBlank() && !state.description.isNullOrBlank()
    }
}
