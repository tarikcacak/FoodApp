package com.example.foodapp.fragments.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!


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
    }
    
    private fun onAddClickListeners() {
        binding.tvAddFoodBreakfast.setOnClickListener { 
            findNavController().navigate(R.id.action_todayFragment_to_addFragment)
        }
        binding.tvAddFoodLunch.setOnClickListener {
            findNavController().navigate(R.id.action_todayFragment_to_addFragment)
        }
        binding.tvAddFoodDinner.setOnClickListener {
            findNavController().navigate(R.id.action_todayFragment_to_addFragment)
        }
        binding.tvAddFoodSnacks.setOnClickListener {
            findNavController().navigate(R.id.action_todayFragment_to_addFragment)
        }
    }

}