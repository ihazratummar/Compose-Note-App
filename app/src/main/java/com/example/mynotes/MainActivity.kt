package com.example.mynotes

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.ui.event.NoteEvent
import com.example.mynotes.ui.navigation.NavigationGraph
import com.example.mynotes.ui.navigation.Route
import com.example.mynotes.ui.theme.MyNotesTheme
import com.example.mynotes.util.LocaleContextWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModels<NoteViewModel>().value
            val state by viewModel.state.collectAsState()
            val context = LocalContext.current
            val updatedContext = remember(state.currentLanguage) {
                LocaleContextWrapper.wrap(context, state.currentLanguage)
            }
            MyNotesTheme(
                darkTheme = state.isDarkMode,
            ) {
                CompositionLocalProvider(
                    LocalContext provides  updatedContext
                ) {
                    val navController = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavigationGraph(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = Route.NoteScreen.route,
                            state = state,
                            event = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}

