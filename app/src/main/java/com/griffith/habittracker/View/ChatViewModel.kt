package com.griffith.habittracker.View

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val TAG = "ChatViewModel"
    private val apiKey = "AIzaSyD9kgsuqAIzaGl16l-jg6_ZBuE4XPw_QyE"

    private val generativeModel by lazy {
        try {
            GenerativeModel(
                // Try without "models/" prefix
                modelName = "gemini-1.0-pro",
                apiKey = apiKey
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error creating model: ${e.message}")
            null
        }
    }

    fun sendMessage(question: String) {
        Log.d("DEBUG_TEST", "sendMessage called with: $question")

        viewModelScope.launch {
            try {
                Log.d("DEBUG_TEST", "About to call API")

                // Try different model formats
                try {
                    val model = GenerativeModel(
                        modelName = "gemini-1.5-pro",  // Try newer model version
                        apiKey = apiKey
                    )

                    Log.d("DEBUG_TEST", "Model created, generating content")
                    val response = model.generateContent(question)
                    Log.d("DEBUG_TEST", "Response received: ${response.text}")
                    return@launch
                } catch (e: Exception) {
                    Log.d("DEBUG_TEST", "First attempt failed, trying second model format")
                }

                // Second attempt with different format
                try {
                    val model = GenerativeModel(
                        modelName = "gemini-pro-vision",  // Try different model
                        apiKey = apiKey
                    )

                    Log.d("DEBUG_TEST", "Second model created, generating content")
                    val response = model.generateContent(question)
                    Log.d("DEBUG_TEST", "Response received: ${response.text}")
                } catch (e: Exception) {
                    Log.e("DEBUG_TEST", "Second attempt failed: ${e.message}")
                }

            } catch (e: Exception) {
                Log.e("DEBUG_TEST", "API Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }}
