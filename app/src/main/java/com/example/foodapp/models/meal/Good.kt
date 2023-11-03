package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class Good(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("indented")
    val indented: Boolean,
    @SerializedName("percentOfDailyNeeds")
    val percentOfDailyNeeds: Double,
    @SerializedName("title")
    val title: String
)