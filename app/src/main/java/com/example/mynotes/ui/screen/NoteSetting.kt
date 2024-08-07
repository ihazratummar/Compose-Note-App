package com.example.mynotes.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mynotes.R
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSetting(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        event(NoteEvent.RefreshNotes)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimens.size8)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        event(NoteEvent.LanguageDialog)
                    },
                shape = RoundedCornerShape(dimens.size15)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens.size20, vertical = dimens.size15),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.languageicon),
                        contentDescription = "Language"
                    )
                    Spacer(modifier = Modifier.width(dimens.size10))
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.change_language)
                    )
                }
            }
        }
        if (state.isLanguageDialogBoxOpen) {
            BasicAlertDialog(onDismissRequest = { event(NoteEvent.LanguageDialog) }) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(dimens.size1, MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                event(NoteEvent.ChangeLanguage("bn"))
                                event(NoteEvent.LanguageDialog)
                            }) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.bengali),
                                    contentDescription = "Bengali"
                                )
                                Spacer(modifier = Modifier.width(dimens.size10))
                                Text(text = "Bengali")
                            }
                        }
                        HorizontalDivider(thickness = 1.dp)
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                event(NoteEvent.ChangeLanguage("en"))
                                event(NoteEvent.LanguageDialog)
                            }) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.english),
                                    contentDescription = "English"
                                )
                                Spacer(modifier = Modifier.width(dimens.size10))
                                Text(text = "English")
                            }
                        }
                        HorizontalDivider(thickness = 1.dp)
                        IconButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                event(NoteEvent.ChangeLanguage("hi"))
                                event(NoteEvent.LanguageDialog)
                            }) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.english),
                                    contentDescription = "Hindi"
                                )
                                Spacer(modifier = Modifier.width(dimens.size10))
                                Text(text = "हिंदी")
                            }
                        }
                    }
                }
            }
        }
    }
}