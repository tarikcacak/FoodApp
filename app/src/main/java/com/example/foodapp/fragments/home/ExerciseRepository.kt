package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.Exercise

interface ExerciseRepository {

    fun getAllExercises(): LiveData<List<Exercise>>
    suspend fun deleteAllExercises()

}