package com.example.mynotes.ui.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.mynotes.domain.model.Note
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.screen.component.NoteCard
import com.example.mynotes.ui.theme.dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    state: NoteState,
    onNoteClick: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(start = dimens.size20),
                        text = "Bookmark",
                        textAlign = TextAlign.Center
                    )
                },
            )
        },
    ) { paddingValues ->
        if (state.notes.none { it.isBookmarked }) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Notes")
            }
        } else {
            if (state.isToggleView){
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.padding(paddingValues).padding(horizontal = dimens.size10),
                    columns = StaggeredGridCells.Fixed(count = 2),
                    horizontalArrangement = Arrangement.spacedBy(dimens.size8),
                ) {
                    items(state.notes.filter { it.isBookmarked }) { notes ->
                        BookmarkNoteCard(notes, onNoteClick = onNoteClick)
                    }
                }
            }else{
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth().padding(paddingValues).padding(horizontal = dimens.size20, vertical = dimens.size4)
                        .fillMaxHeight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimens.size8)
                ){
                    items(state.notes.filter { it.isBookmarked }) { note ->
                        NoteCard(note = note, onNoteClick = onNoteClick )
                    }
                }
            }
        }
    }
}


@Composable
fun BookmarkNoteCard(
    note: Note,
    onNoteClick: (Int) -> Unit = {}
) {

    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(note.dateAdded))
    Card(
        modifier = Modifier
            .clickable {
                onNoteClick(note.id)
            }
            .padding(dimens.size5),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size15)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = dimens.size8)
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                modifier = Modifier.padding(top = dimens.size8),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = dimens.size8)
            )
        }
    }
}