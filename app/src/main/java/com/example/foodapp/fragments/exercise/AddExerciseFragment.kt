package com.example.foodapp.fragments.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.databinding.FragmentAddExerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExerciseFragment : Fragment() {

    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddExerciseViewModel by activityViewModels()
    private var goal: Int = 0
    private lateinit var selectedSpinnerItem: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddExerciseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinner()
        getOnClickData()
        onBtnClickListeners()
    }

    private fun getOnClickData() {
        val args = this.arguments
        goal = args?.getInt("goal")!!.toInt()
    }

    private fun onBtnClickListeners() {
        binding.btnAdd.setOnClickListener {
            val exerciseCalories = binding.trainingEditText.text

            val exercise = Exercise(
                selectedSpinnerItem,
                exerciseCalories.toString(),
                0
            )
            viewModel.addExercise(exercise)

            val bundle = Bundle().apply {
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_addExerciseFragment_to_todayFragment, bundle)
        }
        binding.btnCancel.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("goal", goal)
            }
            findNavController().navigate(R.id.action_addExerciseFragment_to_todayFragment, bundle)
        }
    }

    private fun setSpinner() {
        val items = arrayOf("Cardio", "Weight")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner: Spinner = binding.spinnerType
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                selectedSpinnerItem = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Gender can't be empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}