/*
package com.snofed.publicapp.maps

import com.google.gson.Gson
import java.util.UUID


fun createDynamicWorkout(): String? {
    // Create dynamic values
    val workoutId = UUID.randomUUID().toString()
    val userId = UUID.randomUUID().toString()
    val activityId = UUID.randomUUID().toString()

    val activity = Activity(
        id = activityId,
        syncAction = 1,
        name = "Morning Run",
        nameTranslates = mapOf(
            "en" to "Morning Run",
            "sv" to "Morgonlöpning",
            "de" to "Morgenlauf",
            "no" to "Morgentur"
        ),
        description = "A nice morning run.",
        coverPath = "/images/cover.png",
        iconPath = "/icons/run.png",
        available = true,
        intervalType = 0
    )

    val workoutPoints = listOf(
        WorkoutPoint(
            id = UUID.randomUUID().toString(),
            workoutRef = workoutId,
            longitude = 12.34,
            latitude = 56.78,
            speed = 5.5,
            timestamp = "2024-08-29T13:45:32.418Z"
        )
    )

    val workoutImages = listOf(
        WorkoutImage(
            id = UUID.randomUUID().toString(),
            syncAction = 1,
            path = "/images/workout1.png",
            workoutId = workoutId
        )
    )

    val workout = Workout(
        id = workoutId,
        syncAction = 1,
        publicUserId = userId,
        publisherFullname = "John Doe",
        duration = 3600,
        distance = 10000,
        isPublic = true,
        averagePace = 6,
        startTime = "2024-08-29T13:45:32.418Z",
        calories = 500,
        activityId = activityId,
        activity = activity,
        description = "Great workout!",
        workoutPoints = workoutPoints,
        leaderboardTime = 1800,
        workoutImages = workoutImages
    )

    // Convert the data class to JSON
    val gson = Gson()
    return gson.toJson(workout)
}

*/
