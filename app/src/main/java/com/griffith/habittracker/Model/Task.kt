package com.griffith.habittracker.Model

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false,
    val isDefault: Boolean = false // To distinguish between default and custom tasks
)

