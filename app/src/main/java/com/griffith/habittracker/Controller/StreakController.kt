package com.griffith.habittracker.Controller

import androidx.compose.runtime.mutableStateOf
import java.util.concurrent.TimeUnit

object StreakController {
    // Using a singleton object to maintain streak across screens
    val streakStartTime = mutableStateOf<Long?>(null)

    // Time components
    val days = mutableStateOf(0)
    val hours = mutableStateOf(0)
    val minutes = mutableStateOf(0)
    val seconds = mutableStateOf(0)

    // Start the streak timer
    fun startStreak() {
        streakStartTime.value = System.currentTimeMillis()
        updateTimeComponents()
    }

    // Record a relapse and restart timer
    fun recordRelapse() {
        // Reset and restart
        streakStartTime.value = System.currentTimeMillis()
        updateTimeComponents()
    }

    // Calculate and update time components
    fun updateTimeComponents() {
        if (streakStartTime.value == null) {
            days.value = 0
            hours.value = 0
            minutes.value = 0
            seconds.value = 0
            return
        }

        val currentTime = System.currentTimeMillis()
        val elapsedMillis = currentTime - streakStartTime.value!!

        // Convert to days, hours, minutes, seconds
        days.value = TimeUnit.MILLISECONDS.toDays(elapsedMillis).toInt()
        val remainingHours = elapsedMillis - TimeUnit.DAYS.toMillis(days.value.toLong())
        hours.value = TimeUnit.MILLISECONDS.toHours(remainingHours).toInt()
        val remainingMinutes = remainingHours - TimeUnit.HOURS.toMillis(hours.value.toLong())
        minutes.value = TimeUnit.MILLISECONDS.toMinutes(remainingMinutes).toInt()
        val remainingSeconds = remainingMinutes - TimeUnit.MINUTES.toMillis(minutes.value.toLong())
        seconds.value = TimeUnit.MILLISECONDS.toSeconds(remainingSeconds).toInt()
    }
}