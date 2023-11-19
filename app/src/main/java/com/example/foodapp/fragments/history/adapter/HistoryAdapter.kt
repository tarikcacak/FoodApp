package com.example.foodapp.fragments.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.local.entity.History
import com.example.foodapp.databinding.HistoryItemBinding

class HistoryAdapter() : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var history = ArrayList<History>()

    fun setHistory(history: ArrayList<History>) {
        this.history = history
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.binding.tvItemDate.text = history[position].date
        holder.binding.tvRemainingValue.text = history[position].remaining
        holder.binding.tvBaseGoalValue.text = history[position].goal
        holder.binding.tvFoodValue.text = history[position].food
        holder.binding.tvExerciseValue.text = history[position].exercise
    }

    override fun getItemCount(): Int {
        return history.size
    }

    class HistoryViewHolder(val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root)
}