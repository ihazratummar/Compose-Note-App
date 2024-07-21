package com.example.mynotes.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.screen.AddNoteScreen
import com.example.mynotes.ui.screen.BookmarkScreen
import com.example.mynotes.ui.screen.NoteDetailScreen
import com.example.mynotes.ui.screen.NoteScreen
import com.example.mynotes.ui.screen.NoteSetting

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    state: NoteState,
    event: (NoteEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Route.NoteScreen.route) {
            NoteScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController,
            )
        }
        composable(route = Route.AddNote.route) {
            AddNoteScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController
            )
        }
        composable(route = Route.NoteDetailScreen.route + "/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            val note = state.notes?.find { it.id.toString() == noteId }
            NoteDetailScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController,
                note = note ?: throw IllegalArgumentException("Note not found")
            )
        }
        composable(Route.BookmarkScreen.route) {
            BookmarkScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController
            )
        }
        composable(Route.SettingScreen.route) {
            NoteSetting(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController
            )
        }
    }
}