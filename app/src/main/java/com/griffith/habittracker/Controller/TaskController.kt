package com.griffith.habittracker.Controller

import android.content.Context
import com.griffith.habittracker.Controller.TaskController.getTasks
import com.griffith.habittracker.Model.Task
import org.json.JSONArray
import org.json.JSONObject

object TaskController {
    private const val PREF_NAME = "task_prefs"
    private const val KEY_TASKS = "tasks"

    // Default tasks that everyone gets
    private val defaultTasks = listOf(
        Task("workout", "Exercise for 1 hour", false, true),
        Task("meditate", "Meditate for 10-20 minutes", false, true),
        Task("hydrate", "Drink 2L-3L of water", false, true),
        Task("journal", "Write in your journal", false, true)
    )

    // Get all tasks (default + custom)
    fun getTasks(context: Context): List<Task> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val tasksJson = prefs.getString(KEY_TASKS, "[]")
        val savedTasks = parseTasksFromJson(tasksJson!!)

        // Make sure default tasks are always included
        val result = savedTasks.toMutableList()
        for (defaultTask in defaultTasks) {
            if (result.none { it.id == defaultTask.id }) {
                result.add(defaultTask)
            }
        }
        return result
    }

    // Toggle a task's completion status
    fun toggleTask(context: Context, taskId: String) {
        val tasks = getTasks(context).toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            val task = tasks[index]
            tasks[index] = task.copy(isCompleted = !task.isCompleted)
            saveTasks(context, tasks)
        }
    }

    // Add a new custom task
    fun addTask(context: Context, title: String) {
        val tasks = getTasks(context).toMutableList()
        tasks.add(Task(title = title))
        saveTasks(context, tasks)
    }

    // Save tasks to SharedPreferences
    private fun saveTasks(context: Context, tasks: List<Task>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = tasksToJson(tasks)
        prefs.edit().putString(KEY_TASKS, json).apply()
    }

    // Reset tasks for a new day
    fun resetDailyTasks(context: Context) {
        val tasks = getTasks(context).map { it.copy(isCompleted = false) }
        saveTasks(context, tasks)
    }

    // Helper function to convert tasks to JSON
    private fun tasksToJson(tasks: List<Task>): String {
        val jsonArray = JSONArray()
        for (task in tasks) {
            val jsonObject = JSONObject()
            jsonObject.put("id", task.id)
            jsonObject.put("title", task.title)
            jsonObject.put("isCompleted", task.isCompleted)
            jsonObject.put("isDefault", task.isDefault)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    // Helper function to parse JSON back to tasks
    private fun parseTasksFromJson(json: String): List<Task> {
        val result = mutableListOf<Task>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val task = Task(
                    id = jsonObject.getString("id"),
                    title = jsonObject.getString("title"),
                    isCompleted = jsonObject.getBoolean("isCompleted"),
                    isDefault = jsonObject.getBoolean("isDefault")
                )
                result.add(task)
            }
        } catch (e: Exception) {
            // Return empty list if there's a parsing error
        }
        return result
    }

    fun deleteTask(context: Context, taskId: String) {
        val tasks = getTasks(context).toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            // Don't delete default tasks
            if (!tasks[index].isDefault) {
                tasks.removeAt(index)
                saveTasks(context, tasks)
            }
        }
    }


}

