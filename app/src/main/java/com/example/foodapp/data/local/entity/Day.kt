package com.example.foodapp.data.local.entity

import java.util.Date

data class Day(
    val id: Int,
    val goal: Int,
    val food: Int,
    val exercise: Int,
    val remaining: Int,
    val date: Date
)