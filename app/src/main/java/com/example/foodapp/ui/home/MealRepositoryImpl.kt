package com.example.foodapp.ui.home

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.TodayMeal
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(private val todayMealDao: TodayMealDao):
    MealRepository {
        private lateinit var listOfMeals: LiveData<List<TodayMeal>>

    override fun getAllMeals(): LiveData<List<TodayMeal>> {
        listOfMeals = todayMealDao.getAllMeals()
        return listOfMeals
    }

    override suspend fun deleteAllMeals() {
        todayMealDao.deleteAll()
    }
}