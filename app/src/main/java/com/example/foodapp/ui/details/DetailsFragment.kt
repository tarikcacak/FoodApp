package com.example.foodapp.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.Favorite
import com.example.foodapp.databinding.FragmentDetailsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var id: Int = 0
    private var type: Int = 0
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
        onAddButtonClick()
        onBackPress()
    }

    private fun onBackPress() {
        if (type == 1) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        } else if (type == 2) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_detailsFragment_to_searchFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        } else if (type == 3) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_detailsFragment_to_favoritesFragment)
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        }
    }

    private fun getOnClickData() {
        val args = this.arguments
        type = args?.getInt("type")!!.toInt()
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
                    val carbs = it.data?.caloricBreakdown?.percentCarbs!!.toFloat()
                    val fat = it.data?.caloricBreakdown?.percentFat!!.toFloat()
                    val protein = it.data?.caloricBreakdown?.percentProtein!!.toFloat()
                    pieChartConfig(carbs, fat, protein)
                }
                else -> Unit
            }
        }
    }

    private fun onAddButtonClick() {
        binding.fabAdd.setOnClickListener {
            val favorite = Favorite(
                id,
                img,
                title,
                0
            )
            lifecycleScope.launch {
                viewModel.addFavorite(favorite)
            }
            Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pieChartConfig(carbs: Float, fat: Float, protein: Float) {
        val pieChart: PieChart = binding.pieChart

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(carbs, "Carbs"))
        entries.add(PieEntry(fat, "Fat"))
        entries.add(PieEntry(protein, "Protein"))

        val pieDataSet = PieDataSet(entries, "Nutrients")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(pieDataSet)
        pieChart.setData(pieData)
        pieChart.description.isEnabled = false
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}