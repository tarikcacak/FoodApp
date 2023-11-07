package com.example.foodapp.fragments.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var id: Int = 0
    private lateinit var img: String
    private lateinit var title: String
    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOnClickData()
        observeMealNutrition()
    }

    private fun getOnClickData() {
        val args = this.arguments
        id = args?.getInt("id")!!.toInt()
        img = args?.getString("img").toString()
        title = args?.getString("title").toString()
        viewModel.getNutrition(id)
    }

    @SuppressLint("SetTextI18n")
    private fun observeMealNutrition() {
        viewModel.nutrition.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.piDetails.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.piDetails.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    binding.piDetails.visibility = View.GONE
                    Glide.with(this@DetailsFragment)
                        .load(img)
                        .into(binding.detailsImage)
                    binding.tvMealName.text = title
                    binding.tvCaloriesValue.text = it.data?.calories.toString() + "cal"
                    binding.tvServingSizeValue.text = it.data?.weightPerServing?.amount.toString() +
                            it.data?.weightPerServing?.unit.toString()
                    binding.tvCarbsValue.text = it.data?.carbs
                    binding.tvFatValue.text = it.data?.fat
                    binding.tvProteinValue.text = it.data?.protein.toString()
                    binding.tvCarbsPercent.text = it.data?.caloricBreakdown?.percentCarbs.toString() + "%"
                    binding.tvFatPercent.text = it.data?.caloricBreakdown?.percentFat.toString() + "%"
                    binding.tvProteinPercent.text = it.data?.caloricBreakdown?.percentProtein.toString() + "%"
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