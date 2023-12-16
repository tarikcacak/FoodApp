package com.example.foodapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
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

    private var historyAdded: Boolean = false


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

    @SuppressLint("SetTextI18n")
    private fun observeLiveData() {
        viewModel.usernameState.observe(viewLifecycleOwner) { username ->
            binding.tvWelcome.text = "Welcome,\n$username"
        }
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
                putInt("type", 1)
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
            calSumMeal = 0

            if (meals.isNotEmpty() && !isDateToday(meals[0].date)) {
                viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
                    calSumExercise = 0

                    for (i in exercises) {
                        calSumExercise += i.calories.toInt()
                    }

                    for (i in meals) {
                        calSumMeal += i.calories
                    }

                    binding.tvExerciseValue.text = calSumExercise.toString()
                    binding.tvFoodValue.text = calSumMeal.toString()

                    calculate()

                    if (!historyAdded) {
                        addHistory()
                        historyAdded = true
                    }

                    lifecycleScope.launch {
                        viewModel.deleteAllMeals()
                        viewModel.deleteAllExercises()
                        calculate()
                    }
                    calculate()
                }
            } else {
                viewModel.getAllExercises().observe(viewLifecycleOwner) { exercises ->
                    calSumExercise = 0

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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Calendar.getInstance().time)
        Log.d("VALUES", "$databaseDate $currentDate")
        if (databaseDate != null) {
            return currentDate == databaseDate
        }
        return true
    }

    private fun calculate() {
        try {
            val baseGoalValue = binding.tvBaseGoalValue.text.toString().toInt()
            val foodValue = binding.tvFoodValue.text.toString().toInt()
            val exerciseValue = binding.tvExerciseValue.text.toString().toInt()
            val remaining = baseGoalValue - foodValue + exerciseValue
            Log.d("VALUES", "$baseGoalValue $foodValue $exerciseValue $remaining")
            binding.tvCalorieNum.text = remaining.toString()
            circularProgressConfig(baseGoalValue, foodValue, exerciseValue)

        } catch (e: NumberFormatException) {
            binding.tvCalorieNum.text = "Error"
        }
    }

    private fun addHistory() = lifecycleScope.launch {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val goal = binding.tvBaseGoalValue.text.toString().toInt()
        Log.d("CaloricV", "$goal $calSumMeal $calSumExercise")
        val remaining = goal - calSumMeal + calSumExercise
        val history = History(
            goal.toString(),
            calSumMeal.toString(),
            calSumExercise.toString(),
            remaining.toString(),
            date
        )
        viewModel.addToHistory(history)
        calSumExercise = 0
        calSumMeal = 0
        calculate()
        binding.tvFoodValue.text = "0"
        binding.tvExerciseValue.text = "0"
    }

    private fun circularProgressConfig(baseGoal: Int, foodValue: Int, exerciseValue: Int) {

        val remaining = baseGoal - foodValue + exerciseValue

        binding.circularProgressHome.apply {
            progressMax = baseGoal.toFloat()
            setProgressWithAnimation(remaining.toFloat(), 1000)
            progressBarWidth = 5f
            backgroundProgressBarWidth = 7f
            progressBarColor = android.graphics.Color.RED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}