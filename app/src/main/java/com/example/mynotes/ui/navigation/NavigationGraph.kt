package com.example.mynotes.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
    state: NoteState,
    event: (NoteEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = NoteScreen,
        exitTransition = { ExitTransition.None},
        enterTransition = { EnterTransition.None}
    ) {
        composable<NoteScreen> {
            NoteScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController,
                onNoteClick = {id ->
                    navController.navigate(NoteDetailScreen(id))
                }
            )
        }
        composable<AddNoteScreen> {
            AddNoteScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController
            )
        }
        composable<NoteDetailScreen> { backStackEntry ->
            val noteId = backStackEntry.toRoute<NoteDetailScreen>()
            val note = state.notes.find { it.id == noteId.noteId }
            NoteDetailScreen(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController,
                note = note ?: throw IllegalArgumentException("Note not found")
            )
        }
        composable<BookmarkScreen> {
            BookmarkScreen(
                state = state,
                onNoteClick = {id ->
                    navController.navigate(NoteDetailScreen(id))
                }
            )
        }
        composable<SettingScreen> {
            NoteSetting(
                modifier = modifier,
                state = state,
                event = event,
                navController = navController
            )
        }
    }
}