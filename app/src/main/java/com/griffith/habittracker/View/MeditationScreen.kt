package com.griffith.habittracker.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.griffith.habittracker.Controller.MeditationController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val controller = remember { MeditationController(context) }
    var selectedSound by remember { mutableStateOf("No Sound") }
    var selectedTime by remember { mutableStateOf(5f) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Ensures that the sound stops when user exits the screen
    DisposableEffect(Unit) {
        onDispose {
            controller.stopSound()
        }
    }

    NavigationDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Meditation",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sound selection dropdown
                SoundDropdown(controller, selectedSound) { sound ->
                    selectedSound = sound
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timer slider (1 to 30 minutes)
                Text(text = "Meditation Time: ${selectedTime.toInt()} min", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    valueRange = 1f..30f,
                    steps = 29,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button to start the meditation timer
                Button(
                    onClick = {
                        if (isTimerRunning) {
                            controller.stopSound()
                        } else {
                            controller.playSound(selectedSound)
                        }
                        isTimerRunning = !isTimerRunning
                    }
                ) {
                    Text(text = if (isTimerRunning) "Stop Meditation" else "Start Meditation")
                }
            }
        }
    }
}

// Keep your existing SoundDropdown composable unchanged
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundDropdown(controller: MeditationController, selectedSound: String, onSoundSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedSound,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            controller.getSoundOptions().forEach { sound ->
                DropdownMenuItem(
                    text = { Text(sound) },
                    onClick = {
                        onSoundSelected(sound)
                        expanded = false
                        controller.playSound(sound)
                    }
                )
            }
        }
    }
}