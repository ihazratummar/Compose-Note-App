package com.example.mynotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotes.domain.model.Note
import com.example.mynotes.domain.repository.NoteRepository
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.event.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {


    private val _sortType = MutableStateFlow(SortType.Date)

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _notes = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.Title -> repository.getNotesByTitle()
            SortType.Date -> repository.getNotesByDateAdded()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(NoteState())

    val state = combine(_state, _sortType, _notes) { state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    private fun isFormValid(state: NoteState): Boolean {
        return !state.title.isNullOrBlank() && !state.description.isNullOrBlank()
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.SaveNotes -> {
                val title = _state.value.title
                val description = _state.value.description
                val dateAdded = System.currentTimeMillis()

                val note = Note(
                    title = title?:"New Note",
                    description =  description?:"Description",
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
                val title = _state.value.updateTitle?: event.note.title
                val description = _state.value.updateDescription?: event.note.description

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
                viewModelScope.launch (Dispatchers.IO){
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
                val newSortType = if (_sortType.value == SortType.Date) SortType.Title else SortType.Date
                _sortType.value = newSortType
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
        }
    }

    private fun refreshNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = repository.getAllNotes()
            _allNotes.value  =note
            _state.update {
                it.copy(
                    notes = note,
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
}