package com.example.foodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.TodayMeal

@Database(entities = [TodayMeal::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun todayMealDao(): TodayMealDao

}