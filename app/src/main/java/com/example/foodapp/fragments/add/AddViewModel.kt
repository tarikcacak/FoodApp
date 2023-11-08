package com.example.foodapp.fragments.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.MealNutrition
import com.example.foodapp.models.meal.SearchMeals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {
    private val _searchedMealsList = MutableLiveData<Resource<SearchMeals>>()
    val searchedMealsList: LiveData<Resource<SearchMeals>> = _searchedMealsList

    private val _nutrition = MutableLiveData<Resource<MealNutrition>>()

    private val nutritionMap = mutableMapOf<Int, MealNutrition>()

    fun getSearchedMeals(query: String) = viewModelScope.launch {
        _searchedMealsList.postValue(Resource.Loading())
        val searchResult = retrofitRepository.getSearchMeals(query)
        _searchedMealsList.postValue(searchResult)
        if (searchResult is Resource.Success) {
            searchResult.data?.results?.forEach { meal ->
                if (meal.id != null) {
                    getNutrition(meal.id)
                }
            }
        }
    }

    private fun getNutrition(id: Int) = viewModelScope.launch {
        _nutrition.postValue(Resource.Loading())
        val nutritionData = retrofitRepository.getNutrition(id)
        _nutrition.postValue(nutritionData)
        if (nutritionData is Resource.Success) {
            val nutrition = nutritionData.data
            if (nutrition != null) {
                nutritionMap[id] = nutrition
            }
        }
    }

    fun getNutritionForMeal(id: Int): MealNutrition? {
        return nutritionMap[id]
    }
}