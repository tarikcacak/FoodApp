package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class SearchMeals(
    @SerializedName("number")
    val number: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("results")
    val results: List<SearchResult>,
    @SerializedName("totalResults")
    val totalResults: Int
)