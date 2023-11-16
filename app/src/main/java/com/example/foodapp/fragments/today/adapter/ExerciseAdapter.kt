package com.example.foodapp.fragments.today.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.local.entity.Exercise
import com.example.foodapp.databinding.ExerciseItemBinding

class ExerciseAdapter() : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    var onItemLongClick: ((Exercise) -> Unit)? = null
    private var exercises = ArrayList<Exercise>()

    fun setExercises(exercises: ArrayList<Exercise>) {
        this.exercises = exercises
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(ExerciseItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.binding.tvTypeValue.text = exercises[position].type
        holder.binding.tvCaloriesValue.text = exercises[position].calories
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(exercises[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun onItemLongClickListener(exercise: (Exercise) -> Unit) {
        onItemLongClick = exercise
    }

    class ExerciseViewHolder(val binding: ExerciseItemBinding): RecyclerView.ViewHolder(binding.root)
}