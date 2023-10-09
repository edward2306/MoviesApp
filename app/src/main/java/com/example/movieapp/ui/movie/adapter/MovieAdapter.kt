package com.example.movieapp.ui.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.util.BASE_URL_POSTER_SIZE_185
import com.example.domain.model.Movie
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemLoadingBinding
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.ui.movie.MovieFragment

class MovieAdapter(var items: List<Movie> = ArrayList()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val item: Int = 0
    private val loading: Int = 1
    private val movieFragment: MovieFragment = MovieFragment()

    interface OnItemClickListener {
        fun onItemClick(movieId: Int)
    }

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == item){
            val binding =
                ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MovieViewHolder(binding)
        }else{
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == items.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as RecyclerView.ViewHolder).bind(items[position])
        val model = items[position]
        if(getItemViewType(position) == item){
            val movieVH: MovieViewHolder = holder as MovieViewHolder
            movieVH.bind(model)
        }else{
            val loadingVH: LoadingViewHolder = holder as LoadingViewHolder
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = movieFragment.getString(R.string.error)

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
        }
    }

    fun setOnMovieClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun retryPageLoad() {
        movieFragment.loadNextPage()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillList(items: List<Movie>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                tvTitle.text = movie.original_title
                Glide.with(this@MovieViewHolder.itemView)
                    .asBitmap()
                    .load(BASE_URL_POSTER_SIZE_185 + movie.backdrop_path)
                    .into(ivPoster)

                llMovie.setOnClickListener {
                    onItemClickListener?.onItemClick(movie.id)
                }
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(items.size - 1)
        this.errorMsg = errorMsg
    }

}

