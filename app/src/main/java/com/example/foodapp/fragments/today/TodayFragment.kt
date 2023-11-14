package com.example.foodapp.fragments.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.databinding.FragmentTodayBinding
import com.example.foodapp.fragments.today.adapter.BreakfastAdapter
import com.example.foodapp.fragments.today.adapter.DinnerAdapter
import com.example.foodapp.fragments.today.adapter.LunchAdapter
import com.example.foodapp.fragments.today.adapter.SnacksAdapter

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private var goal: Int = 0
    private lateinit var breakfastAdapter: BreakfastAdapter
    private lateinit var lunchAdapter: LunchAdapter
    private lateinit var dinnerAdapter: DinnerAdapter
    private lateinit var snacksAdapter: SnacksAdapter
    private val viewModel: TodayViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddClickListeners()
        getOnClickData()
        getData()
        setupRecyclerView()
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
    }

    private fun setupRecyclerView() {
        binding.recViewBreakfast.apply {
            adapter = breakfastAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        binding.recViewLunch.apply {
            adapter = lunchAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        binding.recViewDinner.apply {
            adapter = dinnerAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
        binding.recViewSnacks.apply {
            adapter = snacksAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun getData() {
        breakfastAdapter = BreakfastAdapter()
        lunchAdapter = LunchAdapter()
        dinnerAdapter = DinnerAdapter()
        snacksAdapter = SnacksAdapter()
        viewModel.getMealsByType(1).observe(viewLifecycleOwner) {
            breakfastAdapter.setMeals(it as ArrayList<TodayMeal>)
            if (it != null) {
                binding.recViewBreakfast.visibility = View.VISIBLE
            }
        }
        viewModel.getMealsByType(2).observe(viewLifecycleOwner) {
            lunchAdapter.setMeals(it as ArrayList<TodayMeal>)
            if (it != null) {
                binding.recViewLunch.visibility = View.VISIBLE
            }
        }
        viewModel.getMealsByType(3).observe(viewLifecycleOwner) {
            dinnerAdapter.setMeals(it as ArrayList<TodayMeal>)
            if (it != null) {
                binding.recViewDinner.visibility = View.VISIBLE
            }
        }
        viewModel.getMealsByType(4).observe(viewLifecycleOwner) {
            snacksAdapter.setMeals(it as ArrayList<TodayMeal>)
            if (it != null) {
                binding.recViewSnacks.visibility = View.VISIBLE
            }
        }
    }
}