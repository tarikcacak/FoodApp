package com.example.foodapp.ui.exercise

import com.example.foodapp.data.local.entity.Exercise

interface AddExerciseRepository {

    suspend fun addExercise(exercise: Exercise)

}