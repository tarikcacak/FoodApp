package com.example.foodapp.fragments.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.data.local.entity.Favorite
import com.example.foodapp.databinding.FavoriteItemBinding

class FavoritesAdapter() : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    var onItemLongClick: ((Favorite) -> Unit)? = null
    var onItemClick: ((Favorite) -> Unit)? = null
    private var favorites = ArrayList<Favorite>()

    fun setFavorites(favorites: ArrayList<Favorite>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(favorites[position].img)
            .into(holder.binding.ivFavoriteItem)
        holder.binding.tvFavoriteItem.text = favorites[position].title
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(favorites[position])
            true
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(favorites[position])
        }
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setOnFavoriteItemLongClickListener(favorite: (Favorite) -> Unit) {
        onItemLongClick = favorite
    }

    fun setOnFavoriteItemClickListener(favorite: (Favorite) -> Unit) {
        onItemClick = favorite
    }

    class FavoritesViewHolder(val binding: FavoriteItemBinding): RecyclerView.ViewHolder(binding.root)
}