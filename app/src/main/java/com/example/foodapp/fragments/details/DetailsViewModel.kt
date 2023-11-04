package com.example.foodapp.fragments.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.MealNutrition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {

    private val _nutrition = MutableLiveData<Resource<MealNutrition>>()
    val nutrition: LiveData<Resource<MealNutrition>> = _nutrition

    fun getNutrition(id: Int) = viewModelScope.launch {
        _nutrition.postValue(Resource.Loading())
        _nutrition.postValue(retrofitRepository.getNutrition(id))
        Log.d("dataNutrition", _nutrition.toString())
    }

}