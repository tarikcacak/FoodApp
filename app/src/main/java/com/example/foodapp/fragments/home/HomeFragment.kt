package com.example.foodapp.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.airmovies.util.Resource
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.models.meal.Meal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePopularMeals()
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
                }
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}