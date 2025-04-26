package com.griffith.habittracker.View

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.habittracker.Controller.StreakController
import com.griffith.habittracker.Model.UserPreferences

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun OnboardingNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Load streak data when app starts
    LaunchedEffect(Unit) {
        StreakController.loadStreak(context)
    }

    NavHost(navController = navController, startDestination = "onboardingScreenOne") {
        composable("onboardingScreenOne") {
            OnboardingScreenOne(navController, UserPreferences.isOnboardingCompleted(context))
        }
        composable("onboardingScreenTwo") {
            OnboardingScreenTwo(navController)
        }
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable("meditation") {
            MeditationScreen(navController)
        }
        composable("stats") {
            Text("Stats Screen")
        }
        composable("chatbot") {
            ChatbotScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("login/register") {
            Text("Login/Register Screen")
        }
    }
}

@Composable
fun OnboardingScreenOne(navController: NavController, detailsCompleted: Boolean){
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.Start //Instead .CenterHorizontally
    ) {
        Text(
            text = "Welcome to the Habit Tracker App",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(26.dp))
        Text(text = "Track your habits, build better routines and stay motivated!")
        Spacer(modifier = Modifier.height(26.dp))

        // Instructions
        Text(
            text = "Purpose of the app works",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Left
        )
        Text(
            text = "1. Stay over time.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "2. Stay over time.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = "3. Stay over time.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Submit button - modified to check if details are already entered
        Button(onClick = {
            try {
                if (detailsCompleted) {
                    // If details already entered, go straight to dashboard
                    navController.navigate("dashboard")
                } else {
                    // Otherwise show details entry screen
                    navController.navigate("onboardingScreenTwo")
                }
            } catch (e: Exception) {
                Log.e("Navigation", "Error navigating: ${e.message}", e)
            }
        }) {
            Text(text = if (detailsCompleted) "Continue to Dashboard" else "Next")
        }
    }
}

@Composable
fun OnboardingScreenTwo(navController: NavController) {
    val habit = remember { mutableStateOf("") }
    val reason = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enter the your details",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Question 1
        Text(text = "What bad habit you would like to quit?")
        OutlinedTextField(
            value = habit.value,
            onValueChange = { habit.value = it },
            modifier = Modifier.fillMaxWidth()
            //placeholder = {Text(text = "e.g., Smoking, Procrastination")}
        )

        // Question 2
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Why would you quit it?")
        OutlinedTextField(
            value = reason.value,
            onValueChange = { reason.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Submit details button
        Button(onClick = {
            // Save user data and start streak
            UserPreferences.saveUserDetails(context, habit.value, reason.value)
            StreakController.startStreak(context)
            navController.navigate("dashboard")
        }) {
            Text(text = "Start my journey")
        }

        // Go back button
        OutlinedButton(
            onClick = { navController.navigate("onboardingScreenOne") }) {
            Text(text = "Back")
        }
    }
}