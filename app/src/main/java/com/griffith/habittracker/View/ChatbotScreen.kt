package com.griffith.habittracker.View

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.griffith.habittracker.View.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: ChatViewModel = viewModel()
    var message by remember { mutableStateOf("") }

    NavigationDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "AI Chatbot",
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
                // Chat input field
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Type a message") }
                )

                Button(
                    onClick = {
                        Log.d("DEBUG_TEST", "Button clicked with message: $message")
                        viewModel.sendMessage(message)
                        message = ""
                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Send")
                }
            }
        }
    }
}
