package com.griffith.habittracker.View

import android.os.Build
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.griffith.habittracker.Controller.MeditationController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val controller = remember { MeditationController(context) }
    var selectedSound by remember { mutableStateOf("No Sound") }
    var selectedMinutes by remember { mutableStateOf(5) }
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


                // Row containing both pickers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Sound selection column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Select Sound",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        SoundNumberPicker(
                            controller = controller,
                            selectedSound = selectedSound,
                            onSoundSelected = {
                                selectedSound = it
                                controller.playSound(it)
                            }
                        )
                    }

                    // Time selection column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Duration (min)",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        TimeNumberPicker(
                            selectedMinutes = selectedMinutes,
                            onMinutesSelected = { selectedMinutes = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Button to start the meditation timer
                Button(
                    onClick = {
                        if (isTimerRunning) {
                            controller.stopSound()
                        } else {
                            controller.playSound(selectedSound)
                        }
                        isTimerRunning = !isTimerRunning
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = if (isTimerRunning) "Stop Meditation" else "Start Meditation")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SoundNumberPicker(
    controller: MeditationController,
    selectedSound: String,
    onSoundSelected: (String) -> Unit
) {
    val soundOptions = controller.getSoundOptions()

    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = soundOptions.size - 1
                displayedValues = soundOptions.toTypedArray()
                value = soundOptions.indexOf(selectedSound).coerceAtLeast(0)

                // Fix text color visibility
                setTextColor(android.graphics.Color.WHITE)

                setOnValueChangedListener { _, _, newVal ->
                    onSoundSelected(soundOptions[newVal])
                }
            }
        },
        update = { picker ->
            picker.value = soundOptions.indexOf(selectedSound).coerceAtLeast(0)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimeNumberPicker(
    selectedMinutes: Int,
    onMinutesSelected: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 1
                maxValue = 60
                value = selectedMinutes

                // Fix text color visibility
                setTextColor(android.graphics.Color.WHITE)

                setOnValueChangedListener { _, _, newVal ->
                    onMinutesSelected(newVal)
                }
            }
        },
        update = { picker ->
            picker.value = selectedMinutes
        }
    )
}
