package com.example.foodapp.fragments.details

import com.example.foodapp.data.local.daos.FavoriteDao
import com.example.foodapp.data.local.entity.Favorite
import javax.inject.Inject

class FavoriteSaveRepositoryImpl @Inject constructor(private val favoriteDao: FavoriteDao):
    FavoriteSaveRepository {
    override suspend fun addFavorite(favorite: Favorite) {
        favoriteDao.addFavorite(favorite)
    }
}