package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.RandomMeals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {

    private val _randomMealsList = MutableLiveData<Resource<RandomMeals>>()
    val randomMealsList: LiveData<Resource<RandomMeals>> = _randomMealsList

    fun getRandomMeals() = viewModelScope.launch {
        _randomMealsList.postValue(Resource.Loading())
        _randomMealsList.postValue(retrofitRepository.getRandomMeals())
    }
}