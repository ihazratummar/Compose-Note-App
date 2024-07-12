package com.example.mynotes.ui.event

import com.example.mynotes.domain.model.Note

sealed interface NoteEvent {

    object SaveNotes: NoteEvent
    object RefreshNotes: NoteEvent

    //Dialogs
    object ShowDeleteNoteDialog: NoteEvent
    object HideDeleteNoteDialog: NoteEvent

    //AddNotes

    data class SetTitle(val title: String): NoteEvent
    data class SetDescription(val description: String): NoteEvent

    //UpdateNotes

    data class UpdateTitle(val title: String): NoteEvent
    data class UpdateDescription(val description: String): NoteEvent
    data class UpdateNote(val note: Note): NoteEvent

    data class DeleteNote(val note: Note): NoteEvent

    data class SortNotes(val sortType: SortType): NoteEvent

    object ToggleSort : NoteEvent

    data class BookmarkNote(val note: Note): NoteEvent

}

enum class SortType{
    TITLE,
    DATE
}