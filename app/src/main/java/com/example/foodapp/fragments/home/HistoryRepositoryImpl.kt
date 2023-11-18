package com.example.foodapp.fragments.home

import com.example.foodapp.data.local.daos.HistoryDao
import com.example.foodapp.data.local.entity.History
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val historyDao: HistoryDao):
    HistoryRepository {
    override suspend fun addToHistory(history: History) {
        historyDao.addToHistory(history)
    }
}