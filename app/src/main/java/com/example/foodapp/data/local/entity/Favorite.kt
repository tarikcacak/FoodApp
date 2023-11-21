package com.example.foodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    val originalId: Int,
    val img: String,
    val title: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)