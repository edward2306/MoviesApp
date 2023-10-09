package com.example.movieapp.ui.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.Video
import com.example.movieapp.databinding.ItemVideoBinding

class VideoAdapter(
    var items: List<Video> = ArrayList()
) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    private var onVideoClickListener: VideoAdapter.OnVideoClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    interface OnVideoClickListener {
        fun onVideoClick(video: String)
    }

    fun setOnVideoClickListener(onVideoClickListener: VideoAdapter.OnVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillList(items: List<Video>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            with(binding) {
                Glide.with(this@ViewHolder.itemView)
                    .asBitmap()
                    .load(video.key?.let { "https://img.youtube.com/vi/$it/0.jpg" })
                    .into(ivVideoPoster)
                ivVideoPlay.setOnClickListener {
                    video.key?.let { it1 -> onVideoClickListener?.onVideoClick(it1) }
                }
            }
        }
    }
}