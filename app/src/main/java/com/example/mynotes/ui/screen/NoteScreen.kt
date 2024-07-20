package com.example.mynotes.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mynotes.R
import com.example.mynotes.domain.model.Note
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.navigation.Route
import com.example.mynotes.ui.screen.component.SearchNote
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
    navController: NavHostController
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "My Notes")
                        Text(
                            text = "Enjoy note taking",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { navController.navigate(Route.BookmarkScreen.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favourite),
                            contentDescription = "Bookmark"
                        )
                    }
                    OutlinedCard(onClick = { event(NoteEvent.ToggleSort) }) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = state.sortType.name)
                            Spacer(modifier = Modifier.width(4.dp))
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
                    navController.navigate(Route.AddNote.route)
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
                SearchNote(state, event)
                Text(text = "No Notes")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchNote(state, event)
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    columns = StaggeredGridCells.Fixed(count = 2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.notes) { note ->
                        NoteCard(note, navController)
                    }
                }
            }
        }
    }
}




@Composable
fun NoteCard(
    note: Note,
    navController: NavController
) {

    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(note.dateAdded))
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(Route.NoteDetailScreen.route + "/${note.id}")
            }
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}