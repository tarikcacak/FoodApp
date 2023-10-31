package com.example.foodapp.data.remote

import com.example.foodapp.models.meal.RandomMeals
import com.example.foodapp.models.meal.SearchMeals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("recipes/random?apiKey=c88b715ac6b04fd5ba2d14eaa58113e3")
    suspend fun getRandomMeals(): Response<RandomMeals>

    @GET("recipes/complexSearch?apiKey=c88b715ac6b04fd5ba2d14eaa58113e3")
    suspend fun getSearchMeals(@Query("query") query: String): Response<SearchMeals>

}