package com.griffith.habittracker.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.griffith.habittracker.Controller.EmergencyController
import com.griffith.habittracker.Controller.EmergencyController.showEmergencyDialog
import com.griffith.habittracker.Controller.StreakController
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.griffith.habittracker.R
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Update time components every second
    LaunchedEffect(Unit) {
        while (true) {
            StreakController.updateTimeComponents()
            delay(1000) // Update every second
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
                            text = "Dashboard",
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
                    },
                    actions = {
                        // Emergency button
                        Button(
                            onClick = { EmergencyController.showEmergencySupport() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Emergency")
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )

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

                // Streak Counter Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Time Since Last Relapse",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Time display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Days
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${StreakController.days.value}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("DAYS")
                            }

                            // Hours
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${StreakController.hours.value}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("HRS")
                            }

                            // Minutes
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${StreakController.minutes.value}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("MINS")
                            }

                            // Seconds
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${StreakController.seconds.value}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("SECS")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Relapse Button
                Button(
                    onClick = {
                        StreakController.recordRelapse(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Relapse",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // YouTube videos section
                YoutubeSection()

            }
        }
    }



    // Emergency Support Dialog
    if (EmergencyController.showEmergencyDialog.value) {
        val quote = remember { EmergencyController.getRandomQuote() }

        AlertDialog(
            onDismissRequest = { EmergencyController.dismissEmergencySupport() },
            title = {
                Text(
                    "Think about it!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        quote,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { EmergencyController.dismissEmergencySupport() }
                ) {
                    Text("I Feel Better")
                }
            }
        )
    }
}
