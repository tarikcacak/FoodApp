package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class SearchMeals(
    @SerializedName("results")
    val results: List<SearchResult>
)