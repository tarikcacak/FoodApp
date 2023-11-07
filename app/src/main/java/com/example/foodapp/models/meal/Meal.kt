package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String
)