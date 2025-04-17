package com.griffith.habittracker.Model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserPreferences {
    private const val PREF_NAME = "habit_tracker_prefs"
    private const val KEY_HABIT = "habit"
    private const val KEY_REASON = "reason"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveHabitDetails(habit: String, reason: String) {
        sharedPreferences.edit {
            putString(KEY_HABIT, habit)
            putString(KEY_REASON, reason)
            putBoolean(KEY_ONBOARDING_COMPLETED, true)
        }
    }

    fun getHabit(): String = sharedPreferences.getString(KEY_HABIT, "") ?: ""

    fun getReason(): String = sharedPreferences.getString(KEY_REASON, "") ?: ""

    fun isOnboardingCompleted(): Boolean = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
}
