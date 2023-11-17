package com.example.foodapp.fragments.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.databinding.FragmentAddBinding
import com.example.foodapp.fragments.add.adapter.AddAdapter
import com.example.foodapp.models.meal.SearchResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by activityViewModels()
    private lateinit var addAdapter: AddAdapter
    private var type: Int = 0
    private var goal: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = Bundle().apply {
                    putInt("goal", goal)
                }
                findNavController().navigate(R.id.action_addFragment_to_todayFragment, bundle)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAdapter = AddAdapter(viewModel, viewLifecycleOwner)
        getAddData()
        getSearchData()
        onItemClick()
    }

    private fun getAddData() {
        val args = this.arguments
        type = args?.getInt("type")!!.toInt()
        goal = args?.getInt("goal")!!.toInt()
    }

    private fun getSearchData() {
        var searchJob: Job? = null
        binding.etSearchBar.addTextChangedListener { query ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.getSearchedMeals(query.toString())
                prepareRecyclerView()
                observeSearchedMeals()
            }
        }
    }

    private fun prepareRecyclerView() {
        binding.recViewSearch.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = addAdapter
        }
    }

    private fun observeSearchedMeals() {
        viewModel.searchedMealsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    if (it.data?.results != null) {
                        addAdapter.setMeals(it.data.results as ArrayList<SearchResult>)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun onItemClick() {
        addAdapter.onAddItemClickListener { meal, nutrition ->
            val carbs = nutrition.carbs.removeSuffix("g").toDoubleOrNull() ?: 0.0
            val fat = nutrition.fat.removeSuffix("g").toDoubleOrNull() ?: 0.0
            val protein = nutrition.protein.removeSuffix("g").toDoubleOrNull() ?: 0.0

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(Calendar.getInstance().time)

            val todayMeal = TodayMeal(
                meal.title,
                nutrition.weightPerServing?.amount!!.toInt(),
                nutrition.calories.toInt(),
                carbs,
                fat,
                protein,
                type,
                currentDate,
                0
            )
            viewModel.addMeal(todayMeal)
            val bundle = Bundle().apply {
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_addFragment_to_todayFragment, bundle)
            Log.d("AddFr", "Success")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}