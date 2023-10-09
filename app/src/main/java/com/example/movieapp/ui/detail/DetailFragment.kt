package com.example.movieapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.data.util.BASE_URL_POSTER_SIZE_185
import com.example.data.util.BASE_URL_POSTER_SIZE_500
import com.example.domain.model.Movie
import com.example.domain.model.response.ReviewResponse
import com.example.domain.model.response.VideoResponse
import com.example.movieapp.R
import com.example.movieapp.common.utils.invisible
import com.example.movieapp.data.Resource
import com.example.movieapp.databinding.FragmentDetailMovieBinding
import com.example.movieapp.ui.detail.adapter.ReviewAdapter
import com.example.movieapp.ui.detail.adapter.VideoAdapter
import com.example.movieapp.ui.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment(R.layout.fragment_detail_movie), VideoAdapter.OnVideoClickListener {
    private lateinit var binding: FragmentDetailMovieBinding
    private val detailMovieViewModel: DetailViewModel by viewModel()
    private val reviewAdapter: ReviewAdapter by inject()
    private val videoAdapter: VideoAdapter by inject()
    private val args: DetailFragmentArgs by navArgs()
    private var HorizontalLayout: LinearLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFetchData()
        setupObserverMovie()
        setupObserverReview()
        setupObserverVideo()
        setupRecyclerView()
        setupSwipeRefresh()
    }

    private fun setupFetchData() {
        detailMovieViewModel.fetchMovie(args.id)
        detailMovieViewModel.fetchReviews(args.id, 1)
        detailMovieViewModel.fetchVideo(args.id)
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentDetailMovie.setOnRefreshListener {
            detailMovieViewModel.fetchMovie(args.id)
            detailMovieViewModel.fetchVideo(args.id)
            detailMovieViewModel.fetchReviews(args.id, 1)
        }
    }

    private fun setupObserverMovie() {

        viewLifecycleOwner.lifecycleScope.launch {
            detailMovieViewModel.movieState.collect {
                handleMovieByIdDataState(it)
            }
        }
    }

    private fun setupObserverReview() {

        viewLifecycleOwner.lifecycleScope.launch {
            detailMovieViewModel.reviewState.collect {
                handleReviewDataState(it)
            }
        }
    }

    private fun setupObserverVideo() {

        viewLifecycleOwner.lifecycleScope.launch {
            detailMovieViewModel.videoState.collect {
                handleVideoDataState(it)
            }
        }
    }

    private fun handleMovieByIdDataState(state: Resource<Movie>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
                state.data?.let {
                    loadDetailMovie(it)
                }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }
        }
    }

    private fun handleReviewDataState(state: Resource<ReviewResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
                state.data?.let {
                    loadReviewMovie(it)
                }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }
        }
    }

    private fun handleVideoDataState(state: Resource<VideoResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
                state.data?.let {
                    loadVideoMovie(it)
                }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentDetailMovie?.isRefreshing = false
            }
        }
    }

    private fun loadDetailMovie(movie: Movie) {
        with(binding) {
            Glide.with(this@DetailFragment)
                .asBitmap()
                .load(BASE_URL_POSTER_SIZE_500 + movie.backdrop_path)
                .into(backdrop)
            Glide.with(this@DetailFragment)
                .asBitmap()
                .load(BASE_URL_POSTER_SIZE_185 + movie.poster_path)
                .into(poster)
            title.text = movie.title
            date.text = movie.release_date
            tvDetailOverview.text = movie.overview
        }
    }

    private fun loadReviewMovie(review: ReviewResponse) {
        review.let {
            reviewAdapter.clear()
            it.results.let { its -> its?.let { it1 -> reviewAdapter.fillList(it1) } }
            if (it.results?.isEmpty() == true) {
                binding.tvDetailReview.invisible()
            }
        }
    }


    private fun loadVideoMovie(video: VideoResponse) {
        video.let {
            videoAdapter.clear()
            it.results.let { its -> its?.let { it1 -> videoAdapter.fillList(it1) } }
        }
    }

    private fun setupRecyclerView() {
        videoAdapter.setOnVideoClickListener(this)
        with(binding) {
            HorizontalLayout = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvVideo.setLayoutManager(HorizontalLayout)
            rvReview.adapter = reviewAdapter
            rvVideo.adapter = videoAdapter
        }

    }

    override fun onVideoClick(video: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/watch?v=${video}")
        startActivity(Intent.createChooser(intent, "Watch Video"))
    }
}