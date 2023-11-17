package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.TodayMeal

interface MealRepository {

    fun getAllMeals(): LiveData<List<TodayMeal>>
    suspend fun deleteAllMeals()

}