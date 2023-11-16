package com.example.foodapp.fragments.today

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.Exercise

interface TodayExerciseRepository {

    fun getExercises(): LiveData<List<Exercise>>
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun deleteAll()

}