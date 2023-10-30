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

    suspend fun getRandomMeals() = safeApiCall { apiService.getRandomMeals() }


    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = api()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    Resource.Error("Something went wrong")
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("Something went wrong")
                    }
                    is HttpException -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("Http exception")
                    }
                    else -> {
                        Log.e("Retrofit", throwable.message.toString())
                        Resource.Error("Something went wrong")
                    }
                }
            }
        }
    }

}