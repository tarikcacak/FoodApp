package com.example.foodapp.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.util.Resource
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.fragments.search.adapter.SearchAdapter
import com.example.foodapp.models.meal.SearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSearchedData()
    }

    private fun getSearchedData() {
        var searchJob: Job? = null
        binding.etSearchBar.addTextChangedListener { query ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.getSearchedMeals(query.toString())
                prepareRecyclerView()
                observeSearchedMeals()
                onSearchedMealClick()
            }
        }
    }

    private fun prepareRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.recViewSearch.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }

    private fun observeSearchedMeals() {
        viewModel.searchedMealsList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    if (it.data?.results != null) {
                        searchAdapter.setMeals(it.data.results as ArrayList<SearchResult>)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun onSearchedMealClick() {
        searchAdapter.setOnSearchItemClickListener { meal ->
            val bundle = Bundle().apply {
                putInt("id", meal.id)
                putString("img", meal.image)
                putString("title", meal.title)
            }
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}