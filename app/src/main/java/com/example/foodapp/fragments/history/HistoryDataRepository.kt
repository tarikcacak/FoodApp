package com.example.foodapp.fragments.history

import androidx.lifecycle.LiveData
import com.example.foodapp.data.local.entity.History

interface HistoryDataRepository {

    fun getHistory(): LiveData<List<History>>

}