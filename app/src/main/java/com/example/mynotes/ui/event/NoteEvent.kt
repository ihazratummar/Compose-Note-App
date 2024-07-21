package com.example.mynotes.ui.event

import com.example.mynotes.domain.model.Note

sealed interface NoteEvent {

    data object SaveNotes: NoteEvent
    data object RefreshNotes: NoteEvent

    //Dialogs
    data object ShowDeleteNoteDialog: NoteEvent
    data object HideDeleteNoteDialog: NoteEvent

    //AddNotes

    data class SetTitle(val title: String): NoteEvent
    data class SetDescription(val description: String): NoteEvent

    //UpdateNotes

    data class UpdateTitle(val title: String): NoteEvent
    data class UpdateDescription(val description: String): NoteEvent
    data class UpdateNote(val note: Note): NoteEvent

    data class DeleteNote(val note: Note): NoteEvent

    data class SortNotes(val sortType: SortType): NoteEvent

    data object ToggleSort : NoteEvent

    data object ToggleView: NoteEvent

    data class BookmarkNote(val note: Note): NoteEvent

    data class SetSearchQuery(val query: String): NoteEvent

    data object ToggleTheme : NoteEvent

    data class ChangeLanguage(val language: String): NoteEvent

    data object LanguageDialog: NoteEvent

}

enum class SortType{
    Title,
    Date
}