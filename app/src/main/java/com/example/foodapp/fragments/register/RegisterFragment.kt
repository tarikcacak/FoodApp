package com.example.foodapp.fragments.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.models.user.User
import com.example.foodapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by activityViewModels()
    private lateinit var selectedSpinnerItem: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_registerFragment_to_optionFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()
        onRegisterButtonClickListener()
        observeRegister()
        observeValidation()
    }

    private fun onRegisterButtonClickListener() {
        binding.apply {
            btnRegister.setOnClickListener {
                val user = User(
                    usernameEditText.text.toString().trim(),
                    emailEditText.text.toString().trim(),
                    passwordEditText.text.toString(),
                    selectedSpinnerItem,
                    ageEditText.text.toString().trim(),
                    weightEditText.text.toString().trim(),
                    hightEditText.text.toString().trim()
                )
                viewModel.createAccountWithEmailAndPassword(user)
            }
        }
    }

    private fun observeRegister() {
        viewModel.observeRegisterLiveData().observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.pbRegister.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pbRegister.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    binding.pbRegister.visibility = View.GONE
                    Toast.makeText(requireContext(), "Successfully registered", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        })
    }

    private fun observeValidation() {
        viewModel.observeValidationLiveData().observe(viewLifecycleOwner, Observer { validation ->
            if (validation.username is RegisterValidation.Failed) {
                binding.usernameEditText.apply {
                    requestFocus()
                    binding.usernameTextLayout.helperText = validation.username.message
                }
            }
            if (validation.email is RegisterValidation.Failed) {
                binding.emailEditText.apply {
                    requestFocus()
                    binding.emailTextLayout.helperText = validation.email.message
                }
            }
            if (validation.password is RegisterValidation.Failed) {
                binding.passwordEditText.apply {
                    requestFocus()
                    binding.passwordTextLayout.helperText = validation.password.message
                }
            }
            if (validation.weight is RegisterValidation.Failed) {
                binding.weightEditText.apply {
                    requestFocus()
                    binding.weightTextLayout.helperText = validation.weight.message
                }
            }
            if (validation.hight is RegisterValidation.Failed) {
                binding.hightEditText.apply {
                    requestFocus()
                    binding.hightTextLayout.helperText = validation.hight.message
                }
            }
        })
    }

    private fun setSpinner() {
        val items = arrayOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner: Spinner = binding.genderSpinner
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
}