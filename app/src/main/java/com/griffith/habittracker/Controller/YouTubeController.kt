package com.griffith.habittracker.Controller

import com.griffith.habittracker.Model.YoutubeVideo
import android.content.Context
import android.content.Intent
import android.net.Uri

object YouTubeController {
    // Return a list of motivational videos
    fun getVideos(): List<YoutubeVideo> {
        return listOf(
            YoutubeVideo("The Simplest Daily Routine for Self-Improvement"),
            YoutubeVideo("This morning routine is scientifically proven to make you limitless"),
            YoutubeVideo("How to learn anything faster than everyone"),
            YoutubeVideo("Feeling lost in your twenties"),
            YoutubeVideo("You waste too much time and it needs to stop")
        )
    }

    // Open YouTube app or browser based on title
    fun openYoutubeVideo(title: String, context: Context) {
        val videoId = when(title) {
            "The Simplest Daily Routine for Self-Improvement" -> "9QiE-M1LrZk"
            "This morning routine is scientifically proven to make you limitless" -> "IdTMhWtxdbc"
            "How to learn anything faster than everyone" -> "mgmVOkLVEmw"
            "Feeling lost in your twenties" -> "azaMfAbA3Pc"
            "You waste too much time and it needs to stop" -> "8Mz_stNt5SI"
            else -> return
        }

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://youtu.be/$videoId")
        )
        context.startActivity(intent)
    }
}
