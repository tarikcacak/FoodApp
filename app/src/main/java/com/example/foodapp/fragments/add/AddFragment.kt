package com.example.foodapp.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentAddBinding
import com.example.foodapp.fragments.add.adapter.AddAdapter
import com.example.foodapp.models.meal.SearchResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by activityViewModels()
    private lateinit var addAdapter: AddAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSearchData()
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
        addAdapter = AddAdapter(viewModel, viewLifecycleOwner)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}