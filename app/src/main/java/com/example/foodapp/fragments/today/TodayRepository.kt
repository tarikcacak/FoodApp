package com.example.foodapp.fragments.today

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.TodayMeal

interface TodayRepository {

    fun getMealsByType(type: Int): LiveData<List<TodayMeal>>
    suspend fun deleteMeal(todayMeal: TodayMeal)
    suspend fun deleteAll()

}