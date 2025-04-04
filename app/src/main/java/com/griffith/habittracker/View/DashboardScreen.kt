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
import com.griffith.habittracker.Controller.StreakController
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Update time components every second
    LaunchedEffect(Unit) {
        while(true) {
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
                            text = "Habit Tracker",
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

                Text(text = "Dashboard Content", style = MaterialTheme.typography.headlineLarge)


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
                        StreakController.recordRelapse()
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
            }
        }
    }
}

