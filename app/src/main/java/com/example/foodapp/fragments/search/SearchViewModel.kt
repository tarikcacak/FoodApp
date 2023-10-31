package com.example.foodapp.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.SearchMeals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {
    private val _searchedMealsList = MutableLiveData<Resource<SearchMeals>>()
    val searchedMealsList: LiveData<Resource<SearchMeals>> = _searchedMealsList

    fun getSearchedMeals(query: String) = viewModelScope.launch {
        _searchedMealsList.postValue(Resource.Loading())
        _searchedMealsList.postValue(retrofitRepository.getSearchMeals(query))
    }
}