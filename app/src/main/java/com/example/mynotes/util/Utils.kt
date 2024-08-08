package com.example.mynotes.util

import android.content.Context
import android.content.Intent
import com.example.mynotes.domain.model.Note

fun shareNote(context: Context, note: Note){
    val shareText = "Title: ${note.title}\n\nDescription: ${note.description}"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/*"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    val shareIntent = Intent.createChooser(sendIntent, null).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(shareIntent)
}

