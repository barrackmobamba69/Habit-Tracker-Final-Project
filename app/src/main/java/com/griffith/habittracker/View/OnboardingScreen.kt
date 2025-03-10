package com.griffith.habittracker.View

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun OnboardingNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboardingScreenOne") {
        composable("onboardingScreenOne") {
            OnboardingScreenOne(navController)
        }
        composable("onboardingScreenTwo") {
            OnboardingScreenTwo()
        }
    }
}

@Composable
fun OnboardingScreenOne(navController: NavController){
    val habit = remember { mutableStateOf("") }
    val reason = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to the Habit Tracker App",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Question 1
        Text(text = "What bad habit you would like to quit?")
        OutlinedTextField (
            value = habit.value,
            onValueChange = { habit.value = it },
            modifier = Modifier.fillMaxWidth()
        )

        //Question 2
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Why would you quit it?")
        OutlinedTextField (
            value = reason.value,
            onValueChange = { reason.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { navController.navigate("onboardingScreenTwo") }) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun OnboardingScreenTwo() {
    Text(text = "Lets start your journey!")
}