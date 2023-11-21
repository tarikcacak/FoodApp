package com.example.foodapp.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.foodapp.data.local.entity.Favorite

@Dao
interface FavoriteDao {

    @Upsert
    suspend fun addFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): LiveData<List<Favorite>>

}