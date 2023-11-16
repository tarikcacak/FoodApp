package com.example.foodapp.fragments.today.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.local.entity.TodayMeal
import com.example.foodapp.databinding.TypeItemBinding

class SnacksAdapter() : RecyclerView.Adapter<SnacksAdapter.SnacksViewHolder>() {

    var onItemLongClick: ((TodayMeal) -> Unit)? = null
    private var meals = ArrayList<TodayMeal>()

    fun setMeals(meals: ArrayList<TodayMeal>) {
        this.meals = meals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnacksViewHolder {
        return SnacksViewHolder(TypeItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SnacksViewHolder, position: Int) {
        holder.binding.tvItemTitle.text = meals[position].title
        holder.binding.tvAmountValue.text = meals[position].amount.toString() + "g"
        holder.binding.tvCaloriesValue.text = meals[position].calories.toString() + "cal"
        holder.binding.tvCarbsValue.text = meals[position].carbs.toString() + "g"
        holder.binding.tvFatValue.text = meals[position].fat.toString() + "g"
        holder.binding.tvProteinValue.text = meals[position].protein.toString() + "g"
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(meals[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun onItemLongClickListener(meal: (TodayMeal) -> Unit) {
        onItemLongClick = meal
    }

    class SnacksViewHolder(val binding: TypeItemBinding): RecyclerView.ViewHolder(binding.root)
}