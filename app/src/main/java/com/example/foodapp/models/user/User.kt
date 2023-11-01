package com.example.foodapp.models.user

data class User(
    val username: String,
    val email: String,
    val password: String,
    val weight: String,
    val hight: String,
    val imgPath: String = "",
    val progress: String = "0"
)