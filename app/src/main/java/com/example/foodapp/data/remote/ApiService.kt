package com.example.foodapp.data.remote

import com.example.foodapp.models.meal.RandomMeals
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("recipes/random?apiKey=c88b715ac6b04fd5ba2d14eaa58113e3")
    suspend fun getRandomMeals(): Response<RandomMeals>

}