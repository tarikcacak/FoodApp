package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class RandomMeals(
    @SerializedName("recipes")
    val recipes: List<Meal>
)