package com.example.foodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "today_meal")
data class TodayMeal(
    val title: String,
    val amount: Int,
    val calories: Int,
    val carbs: Double,
    val fat: Double,
    val protein: Double,
    val type: Int,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)