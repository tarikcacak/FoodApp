package com.example.foodapp.ui.details

import com.example.foodapp.data.local.entity.Favorite

interface FavoriteSaveRepository {

    suspend fun addFavorite(favorite: Favorite)

}