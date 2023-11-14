package com.example.foodapp.fragments.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.local.entity.TodayMeal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val todayRepository: TodayRepository): ViewModel() {

    fun getMealsByType(type: Int): LiveData<List<TodayMeal>> {
        return todayRepository.getMealsByType(type)
    }

}