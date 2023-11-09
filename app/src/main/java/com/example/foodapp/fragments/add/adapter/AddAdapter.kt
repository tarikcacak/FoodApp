package com.example.foodapp.fragments.add.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.util.Resource
import com.example.foodapp.databinding.AddItemBinding
import com.example.foodapp.fragments.add.AddViewModel
import com.example.foodapp.models.meal.SearchResult

class AddAdapter(
    val viewModel: AddViewModel,
    val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {

    var onItemClick: ((SearchResult) -> Unit)? = null
    private var mealList = ArrayList<SearchResult>()

    fun setMeals(mealList: ArrayList<SearchResult>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        return AddViewHolder(AddItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].image)
            .into(holder.binding.ivSearchItem)
        holder.binding.tvSearchItem.text = mealList[position].title

        val nutritionData = viewModel.getNutritionForMeal(mealList[position].id)
        if (nutritionData != null) {
            holder.binding.tvAmountValue.text = nutritionData.weightPerServing?.amount.toString() +
                    nutritionData.weightPerServing?.unit.toString()
            holder.binding.tvCalValue.text = nutritionData.calories + "cal"
            holder.binding.tvCarbsValue.text = nutritionData.carbs
            holder.binding.tvFatValue.text = nutritionData.fat
            holder.binding.tvProteinValue.text = nutritionData.protein
        }

    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun onAddItemClickListener(meal: (SearchResult) -> Unit) {
        onItemClick = meal
    }

    class AddViewHolder(var binding: AddItemBinding): RecyclerView.ViewHolder(binding.root)
}