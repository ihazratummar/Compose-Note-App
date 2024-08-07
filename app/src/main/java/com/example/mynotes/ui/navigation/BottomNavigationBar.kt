package com.example.mynotes.ui.navigation

import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavigation.Home,
        BottomNavigation.Bookmark,
    )
    val currentStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentStackEntry?.destination
    val isBottomNavVisible =
        bottomNavItems.any { it.route::class.qualifiedName == currentDestination?.route }
    if (isBottomNavVisible) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            bottomNavItems.forEach { item ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.name
                        )
                    },
                    label = { Text(text = item.name) },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}