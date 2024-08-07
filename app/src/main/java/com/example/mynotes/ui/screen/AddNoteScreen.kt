package com.example.mynotes.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mynotes.R
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.screen.component.TransparentHintTextField
import com.example.mynotes.ui.theme.dimens
import com.example.mynotes.util.Constants.DESCRIPTION_TEXT

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    OutlinedCard(onClick = { navController.popBackStack() }) {
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = {
                    if (state.isAddingFormValid) {
                        event(NoteEvent.SaveNotes)
                        navController.popBackStack()
                        event(NoteEvent.RefreshNotes)
                    }
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_save_24),
                    contentDescription = "Save"
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimens.size10)
                .fillMaxSize()
                .imePadding()
        ) {
            item {
                Spacer(modifier = Modifier.height(dimens.size15))
                TransparentHintTextField(
                    text = state.title ?: "",
                    onValueChange = {
                        event(NoteEvent.SetTitle(it))
                    },
                    hint = if (state.title == null) stringResource(R.string.enter_title_of_note) else "",
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    ),
                    modifier = Modifier
                        .padding(dimens.size4)
                        .fillMaxWidth(),
                    imeAction = ImeAction.Next,
                )
            }
            item {
                Spacer(modifier = Modifier.height(dimens.size15))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(dimens.size15))
            }

            item {
                TransparentHintTextField(
                    text = state.description ?: "",
                    onValueChange = {
                        event(NoteEvent.SetDescription(it))
                    },
                    hint = if (state.description == null) DESCRIPTION_TEXT else "",
                    modifier = Modifier
                        .padding(dimens.size4)
                        .fillParentMaxSize(),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textMotion = TextMotion.Animated,
                        lineHeight = 25.sp
                    ),
                    imeAction = ImeAction.Default,
                )
            }
        }
    }
}