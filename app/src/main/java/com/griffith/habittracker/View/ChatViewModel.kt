package com.griffith.habittracker.View

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.griffith.habittracker.Controller.StreakController
import com.griffith.habittracker.Model.UserAnalytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatItem(val content: String, val isFromUser: Boolean)

class ChatViewModel : ViewModel() {
    private val apiKey = "AIzaSyCEqzITnZv2OjL-VSpgoEiK4Kamh6_eQOw"

    private val _messages = MutableStateFlow(listOf<ChatItem>())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Store conversation history for memory
    private val conversationHistory = mutableListOf<String>()

    fun initializeChat(context: Context) {
        if (_messages.value.isEmpty()) {
            val username = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
                .getString("username", "Champ") ?: "Champ"
            val habit = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("habit", "your habit") ?: "your habit"

            val welcome = "Hi $username! 👋 I'm here to help you quit $habit. How are you feeling today? You can ask me about your progress, need motivation, or just chat!"
            _messages.value = listOf(ChatItem(welcome, false))

            // Add to conversation history
            conversationHistory.add("Assistant: $welcome")
        }
    }

    fun sendMessage(userMessage: String, context: Context) {
        if (userMessage.trim().isEmpty()) return

        Log.d("ChatViewModel", "Sending message: $userMessage")
        _messages.value += ChatItem(userMessage, true)
        _isLoading.value = true

        // Add user message to conversation history
        conversationHistory.add("User: $userMessage")

        viewModelScope.launch {
            try {
                // Check for simple commands first
                val quickResponse = checkSimpleCommands(userMessage, context)
                if (quickResponse != null) {
                    _messages.value += ChatItem(quickResponse, false)
                    conversationHistory.add("Assistant: $quickResponse")
                    _isLoading.value = false
                    return@launch
                }

                Log.d("ChatViewModel", "Creating GenerativeModel...")
                val model = GenerativeModel("gemini-1.5-pro", apiKey)

                // Build prompt with user data and conversation history
                val fullPrompt = buildPromptWithHistory(userMessage, context)

                Log.d("ChatViewModel", "Generating content with history...")
                val response = model.generateContent(fullPrompt)

                val responseText = response.text
                if (responseText.isNullOrBlank()) {
                    Log.w("ChatViewModel", "Empty response received")
                    val errorMsg = "I'm having trouble responding right now. Please try again."
                    _messages.value += ChatItem(errorMsg, false)
                    conversationHistory.add("Assistant: $errorMsg")
                } else {
                    Log.d("ChatViewModel", "Adding response to messages: $responseText")
                    _messages.value += ChatItem(responseText, false)
                    conversationHistory.add("Assistant: $responseText")
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error generating content", e)
                val errorMsg = "I encountered an error: ${e.message}. Please try again."
                _messages.value += ChatItem(errorMsg, false)
                conversationHistory.add("Assistant: $errorMsg")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun buildPromptWithHistory(userMessage: String, context: Context): String {
        val userPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val settingsPrefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

        val username = settingsPrefs.getString("username", "Champ") ?: "Champ"
        val habit = userPrefs.getString("habit", "bad habit") ?: "bad habit"
        val reason = userPrefs.getString("reason", "your health") ?: "your health"
        val currentDays = StreakController.days.value
        val currentHours = StreakController.hours.value
        val currentMinutes = StreakController.minutes.value
        val relapseCount = UserAnalytics.getRelapseCount(context)
        val longestDays = UserAnalytics.getLongestStreakDays(context)

        val contextInfo = """
You are a supportive AI coach/assistant helping someone quit their bad habit and staying motivated.

USER INFO:
- Name: $username
- Habit they're quitting: $habit  
- Their reason for quitting: $reason
- Current streak: $currentDays days, $currentHours hours, $currentMinutes minutes
- Total relapses: $relapseCount
- Longest streak: $longestDays days

INSTRUCTIONS:
- Always remember our conversation history
- Use their name ($username) when appropriate
- Be supportive and encouraging
- Reference their progress and specific habit
- If they tell you new information about themselves, remember it
- Keep responses friendly but not too long

CONVERSATION HISTORY:
${conversationHistory.takeLast(10).joinToString("\n")}

Current user message: $userMessage

Respond helpfully:"""

        return contextInfo
    }

    private fun checkSimpleCommands(message: String, context: Context): String? {
        val msg = message.lowercase().trim()

        val userPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val settingsPrefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

        val username = settingsPrefs.getString("username", "Champ") ?: "Champ"
        val habit = userPrefs.getString("habit", "your habit") ?: "your habit"
        val reason = userPrefs.getString("reason", "your goals") ?: "your goals"

        return when {
            msg.contains("my stats") || msg.contains("progress") || msg.contains("how am i doing") -> {
                val days = StreakController.days.value
                val hours = StreakController.hours.value
                val relapses = UserAnalytics.getRelapseCount(context)
                val longestDays = UserAnalytics.getLongestStreakDays(context)

                "📊 Hey $username! Here's your progress:\n\n🔥 Current streak: $days days, $hours hours\n🏆 Best streak: $longestDays days\n📉 Total relapses: $relapses\n\nYou're doing great! Keep it up! 💪"
            }

            msg.contains("help") || msg.contains("emergency") || msg.contains("urge") || msg.contains("craving") -> {
                "🚨 $username, you got this! Quick help:\n\n💨 Take 5 deep breaths\n🚿 Splash cold water on your face\n🏃 Do 10 jumping jacks\n🚪 Step outside for fresh air\n\nRemember: You're quitting $habit because $reason. This urge will pass! 💪"
            }

            msg.contains("motivation") || msg.contains("motivate me") || msg.contains("encourage") -> {
                val days = StreakController.days.value
                "🔥 $username, you're amazing! You've been free from $habit for $days days!\n\nRemember why you started: $reason\n\nYou're stronger than any urge. Keep going! 🌟"
            }

            msg.contains("my streak") || msg.contains("current streak") -> {
                val days = StreakController.days.value
                val hours = StreakController.hours.value
                val minutes = StreakController.minutes.value

                when {
                    days > 0 -> "🔥 $username, you've been clean for $days days and $hours hours! That's incredible!"
                    hours > 0 -> "🔥 $username, you've been clean for $hours hours and $minutes minutes! Every hour counts!"
                    else -> "🔥 $username, you've been clean for $minutes minutes! You're just getting started - keep going!"
                }
            }

            else -> null
        }
    }
}