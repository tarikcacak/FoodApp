package com.example.foodapp.fragments.favorites

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.data.local.entity.Favorite
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.fragments.favorites.adapter.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val viewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getData()
        onClickListeners()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter()
        binding.recViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun getData() {
        viewModel.getAllFavorite().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                favoritesAdapter.setFavorites(it as ArrayList<Favorite>)
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun onClickListeners() {
        favoritesAdapter.setOnFavoriteItemLongClickListener { favorite ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove ${favorite.title}?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("Remove Meal")
            dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {
                    viewModel.deleteFavorite(favorite)
                    dialog.dismiss()
                }
            }
            dialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
        favoritesAdapter.setOnFavoriteItemClickListener { favorite ->
            val bundle = Bundle().apply {
                putInt("id", favorite.id)
                putString("img", favorite.img)
                putString("title", favorite.title)
            }
            findNavController().navigate(R.id.action_favoritesFragment_to_detailsFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}