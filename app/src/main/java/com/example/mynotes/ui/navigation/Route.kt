package com.example.mynotes.ui.navigation


sealed class Route(val route: String) {

    data object NoteScreen : Route("note_screen")
    data object AddNote : Route("add_note")
    data object BookmarkScreen : Route("bookmark_screen")
    data object NoteDetailScreen: Route("note_detail_screen/{noteId}")
    data object SettingScreen: Route("setting_screen")

}