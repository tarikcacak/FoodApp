package com.example.foodapp.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.data.local.entity.TodayMeal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val todayRepository: TodayRepository,
    private val todayExerciseRepository: TodayExerciseRepository
): ViewModel() {

    fun getMealsByType(type: Int): LiveData<List<TodayMeal>> {
        return todayRepository.getMealsByType(type)
    }

    suspend fun deleteMeal(todayMeal: TodayMeal) {
        todayRepository.deleteMeal(todayMeal)
    }

    suspend fun deleteAllMeals() {
        todayRepository.deleteAll()
    }

    fun getExercises(): LiveData<List<Exercise>> {
        return todayExerciseRepository.getExercises()
    }

    suspend fun deleteExercise(exercise: Exercise) {
        todayExerciseRepository.deleteExercise(exercise)
    }

    suspend fun deleteAllExercises() {
        todayExerciseRepository.deleteAll()
    }

}