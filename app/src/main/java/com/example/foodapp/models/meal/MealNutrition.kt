package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class MealNutrition(
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown,
    @SerializedName("calories")
    val calories: String,
    @SerializedName("carbs")
    val carbs: String,
    @SerializedName("fat")
    val fat: String,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServing
)