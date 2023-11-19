package com.example.foodapp.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.foodapp.data.local.entity.History

@Dao
interface HistoryDao {

    @Upsert
    suspend fun addToHistory(history: History)

    @Query("SELECT * FROM history")
    fun getHistory(): LiveData<List<History>>

    @Query("DELETE FROM history")
    suspend fun deleteAll()

}