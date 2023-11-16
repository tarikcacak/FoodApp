package com.example.foodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.data.local.entity.TodayMeal

@Database(entities = [TodayMeal::class, Exercise::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun todayMealDao(): TodayMealDao
    abstract fun exerciseDao(): ExerciseDao

}