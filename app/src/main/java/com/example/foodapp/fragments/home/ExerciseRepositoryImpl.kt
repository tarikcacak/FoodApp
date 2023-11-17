package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.entity.Exercise
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseDao: ExerciseDao):
    ExerciseRepository {
    private lateinit var listOfExercises: LiveData<List<Exercise>>
    override fun getAllExercises(): LiveData<List<Exercise>> {
        listOfExercises = exerciseDao.getExercises()
        return listOfExercises
    }

    override suspend fun deleteAllExercises() {
        exerciseDao.deleteAllExercises()
    }
}