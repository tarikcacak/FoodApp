package com.example.foodapp.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.History
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.models.meal.Meal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private var genderValue: String = "Male"
    private var ageValue: Int = 0
    private var weightValue: Int = 0
    private var hightValue: Int = 0
    private var calSumMeal: Int = 0
    private var calSumExercise: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calSumMeal = 0
        calSumExercise = 0
        viewModel.getRandomMeals()
        observePopularMeals()
        onTodayClickListener()
        viewModel.getData()
        observeLiveData()
        getGoal()
        roomFunctions()
    }

    private fun observePopularMeals() {
        viewModel.randomMealsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.pbPopular.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    binding.pbPopular.visibility = View.GONE
                }
                is Resource.Success -> {
                    val mealList = it.data?.recipes as ArrayList<Meal>
                    Glide.with(this@HomeFragment)
                        .load(mealList[0].image)
                        .into(binding.ivMeal)
                    binding.pbPopular.visibility = View.GONE
                    onRandomMealClickListener(mealList[0].id, mealList[0].image, mealList[0].title)
                }
                else -> Unit
            }
        }
    }

    private fun observeLiveData() {
        viewModel.genderState.observe(viewLifecycleOwner) { gender ->
            genderValue = gender.toString()
        }
        viewModel.ageState.observe(viewLifecycleOwner) { age ->
            ageValue = age.toString().toInt()
        }
        viewModel.weightState.observe(viewLifecycleOwner) { weight ->
            weightValue = weight.toString().toInt()
        }
        viewModel.hightState.observe(viewLifecycleOwner) { hight ->
            hightValue = hight.toString().toInt()
            getGoal()
        }
    }

    private fun onRandomMealClickListener(id: Int, img: String, title: String) {
        binding.ivMeal.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("id", id)
                putString("img", img)
                putString("title", title)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
    }

    private fun onTodayClickListener() {
        binding.cvCalories.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("goal", binding.tvBaseGoalValue.text.toString().toInt())
            }
            findNavController().navigate(R.id.action_homeFragment_to_todayFragment, bundle)
        }
    }

    private fun getGoal() {
        if (genderValue == "Male") {
            val avgCaloriesMale = 88.362 + (13.397 * weightValue) +
                    (4.799 * hightValue) - (5.677 * ageValue)

            binding.tvBaseGoalValue.text = avgCaloriesMale.toInt().toString()
        }
        if (genderValue == "Female") {
            val avgCaloriesFemale = 447.593 + (9.247 * weightValue) +
                    (3.098 * hightValue) - (4.330 * ageValue)
            binding.tvBaseGoalValue.text = avgCaloriesFemale.toInt().toString()
        }
    }

    private fun roomFunctions() {
        viewModel.getAllMeals().observe(viewLifecycleOwner) { meals ->
            if (meals.isNotEmpty() && !isDateToday(meals[0].date)) {
                viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
                    for (i in exercises) {
                        calSumExercise += i.calories.toInt()
                    }
                    for (i in meals) {
                        calSumMeal += i.calories
                    }
                    binding.tvExerciseValue.text = calSumExercise.toString()
                    binding.tvFoodValue.text = calSumMeal.toString()
                    calculate()
                }
                addHistory()
                lifecycleScope.launch {
                    viewModel.deleteAllMeals()
                    viewModel.deleteAllExercises()
                }
            } else {
                viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
                    for (i in exercises) {
                        calSumExercise += i.calories.toInt()
                    }
                    for (i in meals) {
                        calSumMeal += i.calories
                    }
                    binding.tvExerciseValue.text = calSumExercise.toString()
                    binding.tvFoodValue.text = calSumMeal.toString()
                    calculate()
                }
            }
        }
    }

    private fun isDateToday(databaseDate: String): Boolean {
        val currentDate = "2023-11-20"
        Log.d("VALUES", "$databaseDate $currentDate")
        return currentDate == databaseDate
    }

    private fun calculate() {
        try {
            val baseGoalValue = binding.tvBaseGoalValue.text.toString().toInt()
            val foodValue = binding.tvFoodValue.text.toString().toInt()
            val exerciseValue = binding.tvExerciseValue.text.toString().toInt()
            val remaining = baseGoalValue - foodValue + exerciseValue
            Log.d("VALUES", "$baseGoalValue $foodValue $exerciseValue $remaining")
            binding.tvCalorieNum.text = remaining.toString()
        } catch (e: NumberFormatException) {
            binding.tvCalorieNum.text = "Error"
        }
    }

    private fun addHistory() = lifecycleScope.launch {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val meals = viewModel.getAllMeals().value
        val exercises = viewModel.getAllExercises().value
        var food = 0
        var exercise = 0
        for (i in meals!!) {
            food += i.calories
        }
        for (i in exercises!!) {
            exercise += i.calories.toInt()
        }
        val goal = binding.tvBaseGoalValue.text.toString().toInt()
        val remaining = goal - food + exercise
        val history = History(
            goal.toString(),
            food.toString(),
            exercise.toString(),
            remaining.toString(),
            date
        )
        viewModel.addToHistory(history)
        calSumExercise = 0
        calSumMeal = 0
        binding.tvFoodValue.text = calSumMeal.toString()
        binding.tvExerciseValue.text = calSumExercise.toString()
        calculate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}