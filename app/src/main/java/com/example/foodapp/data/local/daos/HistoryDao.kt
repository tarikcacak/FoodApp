package com.example.foodapp.data.local.daos

import androidx.room.Dao
import androidx.room.Upsert
import com.example.foodapp.data.local.entity.History

@Dao
interface HistoryDao {

    @Upsert
    suspend fun addToHistory(history: History)

}