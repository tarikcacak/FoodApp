package com.example.foodapp.fragments.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.airmovies.util.validateAmount
import com.example.airmovies.util.validateCalories
import com.example.airmovies.util.validateCarbs
import com.example.airmovies.util.validateFat
import com.example.airmovies.util.validateProtein
import com.example.airmovies.util.validateTitle
import com.example.airmovies.util.validateType
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.MealNutrition
import com.example.foodapp.models.meal.SearchMeals
import com.example.foodapp.util.AddMealFieldState
import com.example.foodapp.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
    private val addTodayMealRepository: AddTodayMealRepository
) : ViewModel() {

    private val _searchedMealsList = MutableLiveData<Resource<SearchMeals>>()
    val searchedMealsList: LiveData<Resource<SearchMeals>> = _searchedMealsList

    private val _nutrition = MutableLiveData<Resource<MealNutrition>>()

    private val nutritionMap = mutableMapOf<Int, MealNutrition>()

    private val _validation = Channel<AddMealFieldState>()
    val validation = _validation.receiveAsFlow()

    fun addMeal(todayMeal: TodayMeal) = viewModelScope.launch {

        if (checkValidation(todayMeal)) {
            addTodayMealRepository.addMeal(todayMeal)
            Log.d("AddFrVM", "Success")
        } else {
            val validationCheck = AddMealFieldState(
                validateTitle(todayMeal.title),
                validateAmount(todayMeal.amount),
                validateCalories(todayMeal.calories),
                validateCarbs(todayMeal.carbs),
                validateFat(todayMeal.fat),
                validateProtein(todayMeal.protein),
                validateType(todayMeal.type)
            )
            _validation.send(validationCheck)
            Log.d("AddFrVM", _validation.toString())
        }

    }

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

    private fun checkValidation(todayMeal: TodayMeal): Boolean {
        val titleValidation = validateTitle(todayMeal.title)
        val amountValidation = validateAmount(todayMeal.amount)
        val caloriesValidation = validateCalories(todayMeal.calories)
        val carbsValidation = validateCarbs(todayMeal.carbs)
        val fatValidation = validateFat(todayMeal.fat)
        val proteinValidation = validateProtein(todayMeal.protein)
        val typeValidation = validateType(todayMeal.type)

        return titleValidation is Validation.Success && amountValidation is Validation.Success &&
                caloriesValidation is Validation.Success && carbsValidation is Validation.Success &&
                fatValidation is Validation.Success && proteinValidation is Validation.Success &&
                typeValidation is Validation.Success
    }
}