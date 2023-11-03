package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String
)