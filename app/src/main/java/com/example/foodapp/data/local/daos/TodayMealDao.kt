package com.example.foodapp.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.foodapp.data.local.entity.TodayMeal

@Dao
interface TodayMealDao {

    @Upsert
    suspend fun insertMeal(todayMeal: TodayMeal)

    @Delete
    suspend fun deleteMeal(todayMeal: TodayMeal)

    @Query("SELECT * FROM today_meal where type = :type")
    fun getMealsByType(type: Int): LiveData<List<TodayMeal>>

    @Query("DELETE FROM today_meal")
    suspend fun deleteAll()

}