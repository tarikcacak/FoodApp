package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.History

interface HistoryRepository {

    suspend fun addToHistory(history: History)

    suspend fun deleteAll()

}