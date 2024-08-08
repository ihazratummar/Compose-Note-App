package com.example.mynotes.ui.navigation

import androidx.annotation.DrawableRes
import com.example.mynotes.R
import kotlinx.serialization.Serializable



@Serializable
data object NoteScreen

@Serializable
data object BookmarkScreen

@Serializable
data object AddNoteScreen

@Serializable
data class NoteDetailScreen(val noteId: Int)

@Serializable
data object SettingScreen

@Serializable
sealed class BottomNavigation<T>(val name : String , @DrawableRes val icon: Int, val route : T){

    @Serializable
    data object Home : BottomNavigation<NoteScreen>(name = "Home", icon = R.drawable.home, route = NoteScreen)

    @Serializable
    data object Bookmark : BottomNavigation<BookmarkScreen>(name = "Bookmark", icon = R.drawable.favourite, route = BookmarkScreen)
}