package com.example.foodapp.fragments.history

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.daos.HistoryDao
import com.example.foodapp.data.local.entity.History
import javax.inject.Inject

class HistoryDataRepositoryImpl @Inject constructor(private val historyDao: HistoryDao):
    HistoryDataRepository {
    private lateinit var listOfHistory: LiveData<List<History>>

    override fun getHistory(): LiveData<List<History>> {
        listOfHistory = historyDao.getHistory()
        return listOfHistory
    }
}