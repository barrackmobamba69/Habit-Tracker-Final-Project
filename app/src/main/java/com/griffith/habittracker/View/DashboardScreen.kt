package com.griffith.habittracker.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Get current route from NavController
    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Menu", modifier = Modifier.padding(16.dp))
                }

                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = currentRoute?.destination?.route == "dashboard",
                    onClick = {
                        navController.navigate("dashboard") {
                            launchSingleTop = true // Prevents multiple copies of the same screen
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Meditation") },
                    selected = currentRoute?.destination?.route == "meditation",
                    onClick = {
                        navController.navigate("meditation") {
                            launchSingleTop = true
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Stats") },
                    selected = currentRoute?.destination?.route == "stats",
                    onClick = {
                        navController.navigate("stats") {
                            launchSingleTop = true
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Chatbot") },
                    selected = currentRoute?.destination?.route == "chatbot",
                    onClick = {
                        navController.navigate("chatbot") {
                            launchSingleTop = true
                        }
                    }
                )


                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = currentRoute?.destination?.route == "settings",
                    onClick = {
                        navController.navigate("settings") {
                            launchSingleTop = true
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Login/Register") },
                    selected = currentRoute?.destination?.route == "login/register",
                    onClick = {
                        navController.navigate("login/register") {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        text = "Habit Tracker",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Dashboard Content", style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}
