package com.example.mynotes.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mynotes.R
import com.example.mynotes.domain.model.Note
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.screen.component.DeleteContactDialog
import com.example.mynotes.ui.screen.component.TransparentHintTextField
import com.example.mynotes.ui.theme.dimens
import com.example.mynotes.util.Constants.DESCRIPTION_TEXT

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteDetailScreen(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
    navController: NavHostController,
    note: Note
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    OutlinedCard(onClick = {
                        navController.popBackStack()
                        event(NoteEvent.RefreshNotes)
                    }) {
                        Row(
                            modifier = Modifier.padding(horizontal = dimens.size8, vertical = dimens.size8),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrowleft),
                                contentDescription = "Back"
                            )
                            Spacer(modifier = Modifier.width(dimens.size8))
                            Text(text = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { event(NoteEvent.BookmarkNote(note)) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favourite),
                            contentDescription = "BookMark",
                            tint = if (note.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        event(NoteEvent.ShowDeleteNoteDialog)

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash),
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = {
                    event(NoteEvent.UpdateNote(note))
                    navController.popBackStack()
                    event(NoteEvent.RefreshNotes)
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Save"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
        ) {
            item {
                TransparentHintTextField(
                    text = state.updateTitle ?: note.title,
                    onValueChange = {
                        event(NoteEvent.UpdateTitle(it))
                    },
                    hint = if (state.updateTitle == null && note.title == "") "Enter title of note..." else "",
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    ),
                    modifier = Modifier.padding(dimens.size4),
                    imeAction = ImeAction.Next
                )
            }
            item {
                Spacer(modifier = Modifier.height(dimens.size5))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(dimens.size5))
            }
            item {
                TransparentHintTextField(
                    text = state.updateDescription ?: note.description,
                    onValueChange = {
                        event(NoteEvent.UpdateDescription(it))
                    },
                    hint = if (state.updateDescription == null && note.description == "") DESCRIPTION_TEXT else "",
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(dimens.size5),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textMotion = TextMotion.Animated,
                        lineHeight = 25.sp
                    ),
                    imeAction = ImeAction.Default
                )
            }
            item {

            }
        }
        if (state.isDeletingNote) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DeleteContactDialog(
                    onDismiss = { event(NoteEvent.HideDeleteNoteDialog) },
                    onConfirm = {
                        event(NoteEvent.DeleteNote(note))
                        navController.popBackStack()
                        event(NoteEvent.RefreshNotes)
                    },
                )
            }
        }
    }
}