package com.example.foodapp.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.activites.MainActivity
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.dialog.setupBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_loginFragment_to_optionFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onLoginButtonClickListener()
        onForgotPasswordClickListener()
        observeLogin()
        observeValidation()
        observeResetPassword()
    }

    private fun onLoginButtonClickListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString()
                viewModel.login(email, password)
            }
        }
    }

    private fun observeLogin() {
        viewModel.observeLoginLiveData().observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.pbLogin.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pbLogin.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    binding.pbLogin.visibility = View.GONE
                    Toast.makeText(requireContext(), "Successfully registered", Toast.LENGTH_SHORT).show()
                    Intent(requireActivity(), MainActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                else -> Unit
            }
        })
    }

    private fun observeValidation() {
        viewModel.observeValidationLiveData().observe(viewLifecycleOwner, Observer { validation ->
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
        })
    }

    private fun observeResetPassword() {
        viewModel.observeResetPasswordLiveData().observe(viewLifecycleOwner, Observer { reset ->
            when (reset) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), reset.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Reset link sent!", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        })
    }

    private fun onForgotPasswordClickListener() {
        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                if (!email.isNullOrEmpty()) {
                    viewModel.resetPassword(email)
                    Toast.makeText(requireContext(), "Reset link is sent to your email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didn't give the email", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}