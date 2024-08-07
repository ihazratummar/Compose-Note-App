package com.example.mynotes.ui.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.mynotes.R
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.navigation.AddNoteScreen
import com.example.mynotes.ui.navigation.BookmarkScreen
import com.example.mynotes.ui.navigation.SettingScreen
import com.example.mynotes.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumNoteScreen(
    state: NoteState,
    navController: NavHostController,
    event: (NoteEvent) -> Unit,
    onNoteClick: (Int) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // Ensure the keyboard remains open when notes are empty and search text is not empty
    LaunchedEffect(state.notes, state.searchText) {
        if (state.searchText.isNotEmpty() && state.notes.isEmpty()) {
            keyboardController?.show()
        }
    }
    Scaffold(
        modifier = Modifier.padding(horizontal = dimens.size8),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(R.string.notes))
                        Text(
                            text = "Enjoy note taking",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(SettingScreen)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.setting),
                            contentDescription = "Bookmark"
                        )
                    }
                    IconButton(onClick = { event(NoteEvent.ToggleTheme) }) {
                        if (state.isDarkMode) {
                            Icon(
                                painter = painterResource(id = R.drawable.moon),
                                contentDescription = "Bookmark"
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.sun),
                                contentDescription = "Bookmark"
                            )
                        }
                    }
                    IconButton(onClick = {
                        navController.navigate(BookmarkScreen)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favourite),
                            contentDescription = "Bookmark"
                        )
                    }
                    OutlinedCard(onClick = { event(NoteEvent.ToggleSort) }) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = dimens.size8,
                                vertical = dimens.size5
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = state.sortType.name)
                            Spacer(modifier = Modifier.width(dimens.size5))
                            Icon(
                                painter = painterResource(id = R.drawable.sorticon),
                                contentDescription = "Shorting"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddNoteScreen)
                },
            ) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Save")
            }
        },
    ) { paddingValues ->

        if (state.notes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SearchNote(
                    state = state,
                    event = event
                )
                Text(text = "No Notes")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchNote(
                    state = state,
                    event = event
                )
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    columns = StaggeredGridCells.Fixed(count = 5),
                    contentPadding = PaddingValues(dimens.size1),
                    horizontalArrangement = Arrangement.spacedBy(dimens.size1),
                ) {
                    items(state.notes) { note ->
                        NoteCard(note, onNoteClick =  onNoteClick)
                    }
                }
            }
        }
    }
}
