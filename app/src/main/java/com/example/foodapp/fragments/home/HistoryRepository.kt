package com.example.foodapp.fragments.home

import com.example.foodapp.data.local.entity.History

interface HistoryRepository {

    suspend fun addToHistory(history: History)

}