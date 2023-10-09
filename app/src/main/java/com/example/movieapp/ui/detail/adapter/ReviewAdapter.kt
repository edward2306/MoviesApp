package com.example.movieapp.ui.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Review
import com.example.movieapp.databinding.ItemReviewBinding

class ReviewAdapter(var items: List<Review> = ArrayList()) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillList(items: List<Review>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            with(binding) {
                tvAuthor.text = review.author
                tvContent.text = review.content
            }
        }
    }
}
