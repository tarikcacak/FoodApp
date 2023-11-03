package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class CaloricBreakdown(
    @SerializedName("percentCarbs")
    val percentCarbs: Double,
    @SerializedName("percentFat")
    val percentFat: Double,
    @SerializedName("percentProtein")
    val percentProtein: Double
)