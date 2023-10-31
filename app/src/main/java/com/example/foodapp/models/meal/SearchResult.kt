package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("title")
    val title: String
)