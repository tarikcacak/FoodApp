package com.example.foodapp.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.validateExerciseCalories
import com.example.airmovies.util.validateExerciseType
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.util.ExerciseFieldState
import com.example.foodapp.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val addExerciseRepository: AddExerciseRepository
) : ViewModel() {

    private val _validation = Channel<ExerciseFieldState>()
    val validation = _validation.receiveAsFlow()

    fun addExercise(exercise: Exercise) = viewModelScope.launch {

        if (checkValidation(exercise)) {
            addExerciseRepository.addExercise(exercise)
        } else {
            val validationCheck = ExerciseFieldState(
                validateExerciseType(exercise.type),
                validateExerciseCalories(exercise.calories)
            )
            _validation.send(validationCheck)
        }

    }

    private fun checkValidation(exercise: Exercise): Boolean {
        val typeValidation = validateExerciseType(exercise.type)
        val caloriesValidation = validateExerciseCalories(exercise.calories)

        return typeValidation is Validation.Success && caloriesValidation is Validation.Success
    }
}