package com.example.movieapp.ui.genre.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Genre
import com.example.movieapp.databinding.ItemGenreBinding

class GenreAdapter(var items: List<Genre> = ArrayList()) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(genreId: Int, genreName: String)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    fun setOnGenreClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillList(items: List<Genre>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    inner class ViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            with(binding) {
                tvGenre.text = genre.name

                tvGenre.setOnClickListener {
                    onItemClickListener?.onItemClick(genre.id, genre.name)
                }

            }
        }
    }

    class GenreResponseItemDiffCallback : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }
}