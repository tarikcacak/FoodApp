package com.example.foodapp.fragments.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.foodapp.activites.LoginRegisterActivity
import com.example.foodapp.databinding.FragmentProfileBinding
import com.example.foodapp.dialog.setupEditAgeDialog
import com.example.foodapp.dialog.setupEditWeightDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var profilePictureUri: Uri? = null
    private val  viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData()
        observeLiveData()
        pickImage()
        onLogOutClick()
        editWeight()
        editAge()
    }

    @SuppressLint("SetTextI18n")
    private fun observeLiveData() {
        viewModel.usernameState.observe(viewLifecycleOwner) { username ->
            binding.tvProfile.text = username
        }
        viewModel.imageState.observe(viewLifecycleOwner) { image ->
            Glide.with(this@ProfileFragment)
                .load(image)
                .into(binding.ivProfile)
        }
        viewModel.genderState.observe(viewLifecycleOwner) { gender ->
            binding.tvGenderValue.text = gender.toString()
        }
        viewModel.ageState.observe(viewLifecycleOwner) { age ->
            binding.tvAgeValue.text = age.toString()
        }
        viewModel.weightState.observe(viewLifecycleOwner) { weight ->
            binding.tvWeightValue.text = weight.toString() + "kg"
        }
        viewModel.hightState.observe(viewLifecycleOwner) { hight ->
            binding.tvHightValue.text = hight.toString() + "cm"
        }
        viewModel.progressState.observe(viewLifecycleOwner) { progress ->
            binding.tvProgressValue.text = progress.toString()
        }
        viewModel.errorState.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            Log.e("profileDataError", error.toString())
        }
        viewModel.loadingState.observe(viewLifecycleOwner) {}
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            profilePictureUri = result.data?.data
            viewModel.saveImage(profilePictureUri!!)
            Glide.with(this@ProfileFragment)
                .load(profilePictureUri)
                .into(binding.ivProfile)
        }
    }

    private fun pickImage() {
        binding.ivProfile.setOnLongClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            activityResultLauncher.launch(intent)
            true
        }
    }

    private fun onLogOutClick() {
        binding.tvLogOut.setOnClickListener {
            viewModel.logOut()
            Intent(requireContext(), LoginRegisterActivity::class.java).also { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun editAge() {
        binding.tvAge.setOnLongClickListener {
            setupEditAgeDialog { age ->
                if (!age.isNullOrEmpty()) {
                    viewModel.editAge(age)
                    Toast.makeText(requireContext(), "Age edited!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didnt enter the age!", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        binding.tvAgeValue.setOnLongClickListener {
            setupEditAgeDialog { age ->
                if (!age.isNullOrEmpty()) {
                    viewModel.editAge(age)
                    Toast.makeText(requireContext(), "Age edited!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didnt enter the age!", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    private fun editWeight() {
        binding.tvWeight.setOnLongClickListener {
            setupEditWeightDialog { weight ->
                if (!weight.isNullOrEmpty()) {
                    val currentWeight = StringBuilder()
                    for (character in binding.tvWeightValue.text) {
                        if (character.isDigit()) {
                            currentWeight.append(character)
                        }
                    }
                    val progress = currentWeight.toString().toInt() - weight.toInt() + binding.tvProgressValue.text.toString().toInt()
                    viewModel.editWeight(weight)
                    viewModel.editProgress(progress.toString())
                    Toast.makeText(requireContext(), "Weight edited!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didn't enter the weight!", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        binding.tvWeightValue.setOnLongClickListener {
            setupEditWeightDialog { weight ->
                if (!weight.isNullOrEmpty()) {
                    val currentWeight = StringBuilder()
                    for (character in binding.tvWeightValue.text) {
                        if (character.isDigit()) {
                            currentWeight.append(character)
                        }
                    }
                    val progress = currentWeight.toString().toInt() - weight.toInt() + binding.tvProgressValue.text.toString().toInt()
                    viewModel.editWeight(weight)
                    viewModel.editProgress(progress.toString())
                    Toast.makeText(requireContext(), "Weight edited!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didn't enter the weight!", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}