package com.example.foodapp.fragments.today

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.entity.Exercise
import javax.inject.Inject

class TodayExerciseRepositoryImpl @Inject constructor(private val exerciseDao: ExerciseDao):
    TodayExerciseRepository {
        private lateinit var listOfExercises: LiveData<List<Exercise>>

    override fun getExercises(): LiveData<List<Exercise>> {
        listOfExercises = exerciseDao.getExercises()
        return listOfExercises
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }

    override suspend fun deleteAll() {
        exerciseDao.deleteAllExercises()
    }
}