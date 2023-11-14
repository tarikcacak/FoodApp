package com.example.foodapp.fragments.add

import com.example.foodapp.data.local.entity.TodayMeal

interface AddTodayMealRepository {
    suspend fun addMeal(todayMeal: TodayMeal)
}