package com.example.foodapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.SearchItemBinding
import com.example.foodapp.models.meal.SearchResult

class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var onItemClick: ((SearchResult) -> Unit)? = null
    private var mealList = ArrayList<SearchResult>()

    fun setMeals(mealList: ArrayList<SearchResult>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].image)
            .into(holder.binding.ivSearchItem)

        holder.binding.tvSearchItem.text = mealList[position].title
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setOnSearchItemClickListener(meal: (SearchResult) -> Unit) {
        onItemClick = meal
    }

    class SearchViewHolder(var binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root)
}