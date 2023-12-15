package com.example.foodapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.local.entity.History
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyDataRepository: HistoryDataRepository
): ViewModel() {

    fun getHistory(): LiveData<List<History>> {
        return historyDataRepository.getHistory()
    }

}