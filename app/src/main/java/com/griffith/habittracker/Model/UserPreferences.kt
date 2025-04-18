//package com.griffith.habittracker.Model
//
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.core.content.edit
//
//object UserPreferences {
//    private const val PREF_NAME = "habit_tracker_prefs"
//    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
//    private const val KEY_HABIT = "habit"
//    private const val KEY_REASON = "reason"
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    fun init(context: Context) {
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//    }
//
//    fun setOnboardingCompleted(completed: Boolean) {
//        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
//    }
//
//    fun isOnboardingCompleted(): Boolean {
//        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
//    }
//
//    fun saveHabitDetails(habit: String, reason: String) {
//        sharedPreferences.edit()
//            .putString(KEY_HABIT, habit)
//            .putString(KEY_REASON, reason)
//            .apply()
//    }
//
//    fun getHabit(): String {
//        return sharedPreferences.getString(KEY_HABIT, "") ?: ""
//    }
//
//    fun getReason(): String {
//        return sharedPreferences.getString(KEY_REASON, "") ?: ""
//    }
//}



package com.griffith.habittracker.Model

import android.content.Context

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    private const val KEY_HABIT = "habit"
    private const val KEY_REASON = "reason"

    fun isOnboardingCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun saveUserDetails(context: Context, habit: String, reason: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_HABIT, habit)
            .putString(KEY_REASON, reason)
            .putBoolean(KEY_ONBOARDING_COMPLETED, true)
            .apply()
    }
}