package com.example.mynotes.ui.event

import com.example.mynotes.domain.model.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val updateNote: Note? = null,
    val title: String? = null,
    val description: String? = null,

    val updateTitle: String? = null,
    val updateDescription: String? = null,
    val updateColor: Int? = null,

    val isAddingNote: Boolean = false,
    val isAddingFormValid: Boolean = false,
    val isUpdatingFormValid: Boolean = false,
    val isDeletingNote: Boolean = false,
    val isUpdatingNote: Boolean = false,

    val sortType: SortType = SortType.Title,

    val searchText: String = "",
    val isSearching: Boolean = false,
    var isSearchBarActive: Boolean = false,

    val isToggleView: Boolean = false,

    val isDarkMode: Boolean = false,

    val currentLanguage: String = "en",

    val isLanguageDialogBoxOpen: Boolean = false,
)
