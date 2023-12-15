package com.example.foodapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.local.entity.Favorite
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.MealNutrition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
    private val favoriteSaveRepository: FavoriteSaveRepository
) : ViewModel() {

    private val _nutrition = MutableLiveData<Resource<MealNutrition>>()
    val nutrition: LiveData<Resource<MealNutrition>> = _nutrition

    fun getNutrition(id: Int) = viewModelScope.launch {
        _nutrition.postValue(Resource.Loading())
        _nutrition.postValue(retrofitRepository.getNutrition(id))
    }

    suspend fun addFavorite(favorite: Favorite) {
        favoriteSaveRepository.addFavorite(favorite)
    }

}