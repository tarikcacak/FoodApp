package com.example.foodapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.daos.FavoriteDao
import com.example.foodapp.data.local.daos.HistoryDao
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.data.local.entity.Favorite
import com.example.foodapp.data.local.entity.History
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.util.Converters

@Database(entities = [TodayMeal::class, Exercise::class, History::class, Favorite::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun todayMealDao(): TodayMealDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
}
