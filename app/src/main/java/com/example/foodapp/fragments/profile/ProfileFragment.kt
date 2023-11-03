package com.example.foodapp.fragments.profile

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
    }

    private fun observeLiveData() {
        viewModel.usernameState.observe(viewLifecycleOwner) { username ->
            binding.tvProfile.text = username
        }
        viewModel.imageState.observe(viewLifecycleOwner) { image ->
            Glide.with(this@ProfileFragment)
                .load(image)
                .into(binding.ivProfile)
        }
        viewModel.weightState.observe(viewLifecycleOwner) { weight ->
            binding.tvWeightValue.text = weight.toString()
        }
        viewModel.hightState.observe(viewLifecycleOwner) { hight ->
            binding.tvHightValue.text = hight.toString()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}