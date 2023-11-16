package com.example.foodapp.fragments.exercise

import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.entity.Exercise
import javax.inject.Inject

class AddExerciseRepositoryImpl @Inject constructor(private val exerciseDao: ExerciseDao):
    AddExerciseRepository {
    override suspend fun addExercise(exercise: Exercise) {
        exerciseDao.addExercise(exercise)
    }
}