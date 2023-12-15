package com.example.foodapp.ui.home

import com.example.foodapp.data.local.entity.History

interface HistoryRepository {

    suspend fun addToHistory(history: History)

    suspend fun deleteAll()

}