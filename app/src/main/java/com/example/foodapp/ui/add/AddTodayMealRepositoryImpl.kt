package com.example.foodapp.ui.add

import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.TodayMeal
import javax.inject.Inject

class AddTodayMealRepositoryImpl @Inject constructor(
    private val todayMealDao: TodayMealDao
): AddTodayMealRepository {

    override suspend fun addMeal(todayMeal: TodayMeal) {
        todayMealDao.addMeal(todayMeal)
    }

}