package com.example.foodapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.local.entity.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    fun getAllFavorite(): LiveData<List<Favorite>> {
        return favoritesRepository.getAllFavorite()
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        favoritesRepository.deleteFavorite(favorite)
    }

}