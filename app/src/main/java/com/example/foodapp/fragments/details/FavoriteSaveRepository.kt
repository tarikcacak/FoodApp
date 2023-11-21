package com.example.foodapp.fragments.details

import com.example.foodapp.data.local.entity.Favorite

interface FavoriteSaveRepository {

    suspend fun addFavorite(favorite: Favorite)

}