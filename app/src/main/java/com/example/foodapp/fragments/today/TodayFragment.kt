package com.example.foodapp.fragments.today

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.databinding.FragmentTodayBinding
import com.example.foodapp.fragments.today.adapter.BreakfastAdapter
import com.example.foodapp.fragments.today.adapter.DinnerAdapter
import com.example.foodapp.fragments.today.adapter.ExerciseAdapter
import com.example.foodapp.fragments.today.adapter.LunchAdapter
import com.example.foodapp.fragments.today.adapter.SnacksAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private var goal: Int = 0
    private var calSum: Int = 0
    private var calSumEx: Int = 0
    private lateinit var breakfastAdapter: BreakfastAdapter
    private lateinit var lunchAdapter: LunchAdapter
    private lateinit var dinnerAdapter: DinnerAdapter
    private lateinit var snacksAdapter: SnacksAdapter
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val viewModel: TodayViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentTodayBinding.inflate(inflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_todayFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddClickListeners()
        getOnClickData()
        getData()
        setupRecyclerView()
        onLongItemClickListener()
    }

    private fun calculate() {
        val goal = binding.tvGoalValue.text.toString().toInt()
        val food = binding.tvFoodValue.text.toString().toInt()
        val exercise = binding.tvExerciseValue.text.toString().toInt()
        val remaining = goal - food + exercise
        binding.tvRemainingValue.text = remaining.toString()
    }

    private fun getOnClickData() {
        val args = this.arguments
        goal = args?.getInt("goal")!!.toInt()
        binding.tvGoalValue.text = goal.toString()
    }
    
    private fun onAddClickListeners() {
        binding.tvAddFoodBreakfast.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("type", 1)
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_todayFragment_to_addFragment, bundle)
        }
        binding.tvAddFoodLunch.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("type", 2)
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_todayFragment_to_addFragment, bundle)
        }
        binding.tvAddFoodDinner.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("type", 3)
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_todayFragment_to_addFragment, bundle)
        }
        binding.tvAddFoodSnacks.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("type", 4)
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_todayFragment_to_addFragment, bundle)
        }
        binding.tvAddExercise.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_todayFragment_to_addExerciseFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        breakfastAdapter = BreakfastAdapter()
        binding.recViewBreakfast.apply {
            adapter = breakfastAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        lunchAdapter = LunchAdapter()
        binding.recViewLunch.apply {
            adapter = lunchAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        dinnerAdapter = DinnerAdapter()
        binding.recViewDinner.apply {
            adapter = dinnerAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        snacksAdapter = SnacksAdapter()
        binding.recViewSnacks.apply {
            adapter = snacksAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        exerciseAdapter = ExerciseAdapter()
        binding.recViewExercise.apply {
            adapter = exerciseAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun getData() {
        viewModel.getMealsByType(1).observe(viewLifecycleOwner) {
            breakfastAdapter.setMeals(it as ArrayList<TodayMeal>)
            var calSumBreakfast = 0
            if (it != null) {
                for (i in it) {
                    calSumBreakfast += i.calories
                }
                binding.tvBreakfastCal.text = calSumBreakfast.toString()
                binding.recViewBreakfast.visibility = View.VISIBLE
                calSum += calSumBreakfast
                binding.tvFoodValue.text = calSum.toString()
                calculate()
            }
        }
        viewModel.getMealsByType(2).observe(viewLifecycleOwner) {
            lunchAdapter.setMeals(it as ArrayList<TodayMeal>)
            var calSumLunch = 0
            if (it != null) {
                for (i in it) {
                    calSumLunch += i.calories
                }
                binding.tvLunchCal.text = calSumLunch.toString()
                binding.recViewLunch.visibility = View.VISIBLE
                calSum += calSumLunch
                binding.tvFoodValue.text = calSum.toString()
                calculate()
            }
        }
        viewModel.getMealsByType(3).observe(viewLifecycleOwner) {
            dinnerAdapter.setMeals(it as ArrayList<TodayMeal>)
            var calSumDinner = 0
            if (it != null) {
                for (i in it) {
                    calSumDinner += i.calories
                }
                binding.tvDinnerCal.text = calSumDinner.toString()
                binding.recViewDinner.visibility = View.VISIBLE
                calSum += calSumDinner
                binding.tvFoodValue.text = calSum.toString()
                calculate()
            }
        }
        viewModel.getMealsByType(4).observe(viewLifecycleOwner) {
            snacksAdapter.setMeals(it as ArrayList<TodayMeal>)
            var calSumSnacks = 0
            if (it != null) {
                for (i in it) {
                    calSumSnacks += i.calories
                }
                binding.tvSnacksCal.text = calSumSnacks.toString()
                binding.recViewSnacks.visibility = View.VISIBLE
                calSum += calSumSnacks
                binding.tvFoodValue.text = calSum.toString()
                calculate()
            }
        }
        viewModel.getExercises().observe(viewLifecycleOwner) {
            exerciseAdapter.setExercises(it as ArrayList<Exercise>)
            var calSumExercise = 0
            if (it != null) {
                for (i in it) {
                    calSumExercise += i.calories.toInt()
                }
            }
            binding.tvExerciseCal.text = calSumExercise.toString()
            binding.recViewExercise.visibility = View.VISIBLE
            calSumEx = calSumExercise
            binding.tvExerciseValue.text = calSumEx.toString()
            calculate()
        }
    }

    private fun onLongItemClickListener() {
        breakfastAdapter.onItemLongClickListener { meal ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove ${meal.title}?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Meal")
            dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {
                    viewModel.deleteMeal(meal)
                }
                calSum = 0
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
        lunchAdapter.onItemLongClickListener { meal ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove ${meal.title}?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Meal")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.deleteMeal(meal)
                }
                calSum = 0
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
        dinnerAdapter.onItemLongClickListener { meal ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove ${meal.title}?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Meal")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.deleteMeal(meal)
                }
                calSum = 0
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
        snacksAdapter.onItemLongClickListener { meal ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove ${meal.title}?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Meal")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.deleteMeal(meal)
                }
                calSum = 0
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
        exerciseAdapter.onItemLongClickListener { exercise ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove this exercise?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Exercise")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    viewModel.deleteExercise(exercise)
                }
                calSum = 0
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}