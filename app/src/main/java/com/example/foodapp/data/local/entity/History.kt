package com.example.foodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    val goal: String,
    val food: String,
    val exercise: String,
    val remaining: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)