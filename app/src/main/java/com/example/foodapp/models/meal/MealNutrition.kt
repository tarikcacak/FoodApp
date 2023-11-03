package com.example.foodapp.models.meal


import com.google.gson.annotations.SerializedName

data class MealNutrition(
    @SerializedName("bad")
    val bad: List<Bad>,
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown,
    @SerializedName("calories")
    val calories: String,
    @SerializedName("carbs")
    val carbs: String,
    @SerializedName("expires")
    val expires: Long,
    @SerializedName("fat")
    val fat: String,
    @SerializedName("flavonoids")
    val flavonoids: List<Flavonoid>,
    @SerializedName("good")
    val good: List<Good>,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("isStale")
    val isStale: Boolean,
    @SerializedName("nutrients")
    val nutrients: List<NutrientX>,
    @SerializedName("properties")
    val properties: List<Property>,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServing
)