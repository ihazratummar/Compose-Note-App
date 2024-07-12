package com.example.mynotes.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mynotes.domain.model.Note
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState

@Composable
fun UpdateNoteScreen(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
    navController: NavHostController,
) {

}