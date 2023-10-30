package com.example.foodapp.fragments.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealLayoutBinding
import com.example.foodapp.models.meal.Meal

class PopularMealsAdapter() : RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() {

    var onItemClick: ((Meal) -> Unit)? = null
    private var mealList = ArrayList<Meal>()

    fun setMeals(mealList: ArrayList<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(MealLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].image)
            .into(holder.binding.ivFoodLayout)
        holder.binding.ivFoodLayout.scaleType = ImageView.ScaleType.CENTER_CROP

        holder.binding.tvFoodLayout.text = mealList[position].title
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setOnPopularMealItemClickListener(meal: (Meal) -> Unit) {
        onItemClick = meal
    }

    class PopularMealsViewHolder(var binding: MealLayoutBinding): RecyclerView.ViewHolder(binding.root)
}