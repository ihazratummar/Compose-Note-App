package com.example.mynotes

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mynotes.ui.navigation.BottomNavigation
import com.example.mynotes.ui.navigation.BottomNavigationBar
import com.example.mynotes.ui.navigation.NavigationGraph
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
                    LocalContext provides updatedContext
                ) {
                    val navController = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            BottomNavigationBar(navController)
                        }) { innerPadding ->
                        NavigationGraph(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            state = state,
                            event = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}

