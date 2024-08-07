package com.example.mynotes.ui.screen.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.example.mynotes.R
import com.example.mynotes.domain.model.Note
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.event.NoteState
import com.example.mynotes.ui.theme.WindowType
import com.example.mynotes.ui.theme.dimens
import com.example.mynotes.ui.theme.rememberWindowSize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomTextField(
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardtype: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    singleLive: Boolean = true,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        label = label,
        value = value,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardtype,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions.Default,
        singleLine = singleLive,
        modifier = Modifier.fillMaxWidth()
    )
}


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransparentHintTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle.Default,
    imeAction: ImeAction,
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = modifier,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),

            )
        if (isHintVisible) {
            Text(text = hint, color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

@Composable
fun DeleteContactDialog(
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.delete_note),
    description: String = stringResource(R.string.are_you_sure_you_want_to_delete_this_note),
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Card(
        modifier = modifier.padding(dimens.size12),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.9f),
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimens.size20)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.padding(dimens.size4))
            Text(text = description)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.no),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.width(dimens.size10))
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.yes),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@Composable
fun SearchNote(
    modifier: Modifier = Modifier,
    state: NoteState,
    event: (NoteEvent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size5),
            value = state.searchText,
            onValueChange = { query ->
                event(NoteEvent.SetSearchQuery(query))
            },
            shape = RoundedCornerShape(dimens.size20),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (state.searchText.isNotEmpty() || state.searchText.isNotBlank()) {
                    IconButton(
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            event(NoteEvent.SetSearchQuery(""))
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.x),
                            contentDescription = "Close"
                        )
                    }
                }
            },
            placeholder = { Text(text = stringResource(R.string.search_notes)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )
    }
}



@Composable
fun NoteCard(
    note: Note,
    onNoteClick: (Int) -> Unit = {}
) {

    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(note.dateAdded))
    val window = rememberWindowSize()
    when(window.width){
        WindowType.Compact -> {
            CompactNoteCard(onNoteClick =  onNoteClick, note, formattedDate)
        }
        WindowType.Medium -> {
            MediumNoteCard(onNoteClick =  onNoteClick, note, formattedDate)
        }
        WindowType.Expanded -> {
            ExpandedNoteCard(onNoteClick =  onNoteClick, note, formattedDate)
        }
    }

}

@Composable
private fun CompactNoteCard(
    onNoteClick: (Int) -> Unit = {},
    note: Note,
    formattedDate: String
) {
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
                modifier = Modifier.padding(dimens.size5)
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                modifier = Modifier.padding(dimens.size4),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(dimens.size4)
            )
        }
    }
}

@Composable
private fun MediumNoteCard(
    onNoteClick: (Int) -> Unit = {},
    note: Note,
    formattedDate: String
) {
    Card(
        modifier = Modifier
            .clickable {
                onNoteClick(note.id)
            }
            .padding(dimens.size2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size5)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                modifier = Modifier.padding(horizontal = dimens.size5),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(dimens.size5))
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                modifier = Modifier.padding(horizontal = dimens.size4),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(dimens.size5))
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal= dimens.size4)
            )
        }
    }
}

@Composable
private fun ExpandedNoteCard(
    onNoteClick: (Int) -> Unit = {},
    note: Note,
    formattedDate: String
) {
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
                modifier = Modifier.padding(dimens.size5)
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                modifier = Modifier.padding(dimens.size4),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(dimens.size4)
            )
        }
    }
}