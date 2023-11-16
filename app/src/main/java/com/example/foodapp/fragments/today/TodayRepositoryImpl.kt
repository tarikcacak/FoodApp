package com.example.foodapp.fragments.today

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.TodayMeal
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(private val todayMealDao: TodayMealDao):
    TodayRepository {
        private lateinit var listOfMeals: LiveData<List<TodayMeal>>

    override fun getMealsByType(type: Int): LiveData<List<TodayMeal>> {
        listOfMeals = todayMealDao.getMealsByType(type)
        return listOfMeals
    }

    override suspend fun deleteMeal(todayMeal: TodayMeal) {
        todayMealDao.deleteMeal(todayMeal)
    }

    override suspend fun deleteAll() {
        todayMealDao.deleteAll()
    }
}