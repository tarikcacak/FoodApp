package com.example.foodapp.util

sealed class Validation() {
    object Success: Validation()
    data class Failed(val message: String): Validation()
}

data class AddMealFieldState(
    val title: Validation,
    val amount: Validation,
    val calories: Validation,
    val carbs: Validation,
    val fat: Validation,
    val protein: Validation,
    val type: Validation
)
