package com.example.foodapp.fragments.favorites

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.Favorite

interface FavoritesRepository {

    fun getAllFavorite(): LiveData<List<Favorite>>

    suspend fun deleteFavorite(favorite: Favorite)

}