package com.example.foodapp.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.data.local.entity.History
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.data.remote.RetrofitRepository
import com.example.foodapp.models.meal.RandomMeals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val retrofitRepository: RetrofitRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val mealRepository: MealRepository,
    private val exerciseRepository: ExerciseRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _randomMealsList = MutableLiveData<Resource<RandomMeals>>()
    val randomMealsList: LiveData<Resource<RandomMeals>> = _randomMealsList

    private val _genderState = MutableLiveData<String>()
    val genderState: LiveData<String> = _genderState

    private val _ageState = MutableLiveData<String>()
    val ageState: LiveData<String> = _ageState

    private val _weightState = MutableLiveData<String>()
    val weightState: LiveData<String> = _weightState

    private val _hightState = MutableLiveData<String>()
    val hightState: LiveData<String> = _hightState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    val currentUid = firebaseAuth.currentUser?.uid.toString()

    fun getAllMeals(): LiveData<List<TodayMeal>> {
        return mealRepository.getAllMeals()
    }

    suspend fun deleteAllMeals() {
        mealRepository.deleteAllMeals()
    }

    fun getAllExercises(): LiveData<List<Exercise>> {
        return exerciseRepository.getAllExercises()
    }

    suspend fun deleteAllExercises() {
        exerciseRepository.deleteAllExercises()
    }

    suspend fun addToHistory(history: History) {
        historyRepository.addToHistory(history)
    }

    fun getRandomMeals() = viewModelScope.launch {
        _randomMealsList.postValue(Resource.Loading())
        _randomMealsList.postValue(retrofitRepository.getRandomMeals())
    }

    fun getData() {
        _loadingState.value = true
        firestore.collection("user").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    _loadingState.value = false
                    _errorState.value = exception.localizedMessage
                    Log.e("errorProfile", exception.message.toString())
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents

                        for (document in documentList) {
                            val gender = document.get("gender") as String
                            val age = document.get("age") as String
                            val weight = document.get("weight") as String
                            val hight = document.get("hight") as String
                            _genderState.value = gender
                            _ageState.value = age
                            _weightState.value = weight
                            _hightState.value = hight
                        }
                    }
                }
            }
    }
}