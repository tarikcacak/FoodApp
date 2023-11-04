package com.example.foodapp.data.remote

import com.example.foodapp.models.meal.MealNutrition
import com.example.foodapp.models.meal.RandomMeals
import com.example.foodapp.models.meal.SearchMeals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("recipes/random?apiKey=c88b715ac6b04fd5ba2d14eaa58113e3")
    suspend fun getRandomMeals(): Response<RandomMeals>

    @GET("recipes/complexSearch?apiKey=c88b715ac6b04fd5ba2d14eaa58113e3")
    suspend fun getSearchMeals(@Query("query") query: String): Response<SearchMeals>

    @GET("recipes/{meal_id}/nutritionWidget.json")
    suspend fun getNutrition(@Path("meal_id") mealId: Int, @Query("apiKey") apiKey: String): Response<MealNutrition>

}