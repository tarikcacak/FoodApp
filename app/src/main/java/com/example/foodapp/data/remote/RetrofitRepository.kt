package com.example.foodapp.data.remote

import android.util.Log
import com.example.airmovies.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class RetrofitRepository @Inject constructor(
    private val apiService: ApiService
) {

    private val apiKey = "c88b715ac6b04fd5ba2d14eaa58113e3"

    suspend fun getRandomMeals() = safeApiCall { apiService.getRandomMeals() }
    suspend fun getSearchMeals(query: String) = safeApiCall { apiService.getSearchMeals(query) }
    suspend fun getNutrition(mealId: Int) = safeApiCall { apiService.getNutrition(mealId, apiKey) }


    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = api()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = response.message()
                    val errorCode = response.code()
                    Log.d("TAG", "Request failed with error code $errorCode: $errorMessage. Error body: $errorBody")
                    Resource.Error("Something went wrong!")
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("IO Exception")
                    }
                    is HttpException -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("Http exception")
                    }
                    else -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("Unknown exception")
                    }
                }
            }
        }
    }

}