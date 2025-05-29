package com.griffith.habittracker.View

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatItem(val content: String, val isFromUser: Boolean)

class ChatViewModel : ViewModel() {
    private val apiKey = "AIzaSyD9kgsuqAIzaGl16l-jg6_ZBuE4XPw_QyE"

    private val _messages = MutableStateFlow(listOf(ChatItem("How can I help you today?", false)))
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun sendMessage(userMessage: String) {
        if (userMessage.trim().isEmpty()) return

        _messages.value += ChatItem(userMessage, true)
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val model = GenerativeModel("gemini-1.5-pro", apiKey)
                val response = model.generateContent(userMessage)
                _messages.value += ChatItem(response.text ?: "No response", false)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error: ${e.message}")
                _messages.value += ChatItem("Sorry, couldn't connect", false)
            }
            _isLoading.value = false
        }
    }
}