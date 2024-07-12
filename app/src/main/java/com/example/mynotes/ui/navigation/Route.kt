package com.example.mynotes.ui.navigation


sealed class Route(val route: String) {
    object NoteScreen : Route("note_screen")
    object AddNote : Route("add_note")
    object BookmarkScreen : Route("bookmark_screen")
    object NoteDetailScreen: Route("note_detail_screen/{noteId}")

}