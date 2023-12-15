package com.example.foodapp.ui.favorites

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.FavoriteDao
import com.example.foodapp.data.local.entity.Favorite
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val favoriteDao: FavoriteDao):
    FavoritesRepository {
    private lateinit var listOfFavorites: LiveData<List<Favorite>>
    override fun getAllFavorite(): LiveData<List<Favorite>> {
        listOfFavorites = favoriteDao.getAllFavorite()
        return listOfFavorites
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }
}