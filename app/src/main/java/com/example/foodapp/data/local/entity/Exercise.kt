package com.example.foodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "exercises")
data class Exercise(
    val type: String,
    val calories: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)