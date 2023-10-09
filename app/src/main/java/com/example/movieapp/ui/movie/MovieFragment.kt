package com.example.movieapp.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.response.MovieResponse
import com.example.movieapp.R
import com.example.movieapp.common.recyclerview.PaginationScrollListener
import com.example.movieapp.data.Resource
import com.example.movieapp.databinding.FragmentListMovieBinding
import com.example.movieapp.ui.movie.adapter.MovieAdapter
import com.example.movieapp.ui.movie.viewmodel.MovieViewModel
import com.example.movieapp.util.navigate
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment(R.layout.fragment_list_movie), MovieAdapter.OnItemClickListener{
    private lateinit var binding: FragmentListMovieBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var movieResponse: MovieResponse
    private val movieViewModel: MovieViewModel by viewModel()
    private val movieAdapter: MovieAdapter by inject()
    private val args: MovieFragmentArgs by navArgs()
    private var rowsArrayList = arrayListOf<String>()
    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupSwipeRefresh()
        setupObserver()
        setupRecyclerView()
        setupLoadMoreObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        movieViewModel.fetchMovie(args.id)
        binding.textMovie.text = "Genre ${args.genre} Movie"
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.movieState.collect {
                handleMovieDataState(it)
            }
        }
    }

    private fun setupLoadMoreObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.movieStateLoadMore.collect {
                handleMovieDataLoadMoreState(it)
            }
        }
    }

    private fun handleMovieDataLoadMoreState(state: Resource<MovieResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentListMovie?.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
                state.data?.let {
                    loadMoreMovie(it)
                    movieResponse = it
                }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
            }
        }
    }

    private fun handleMovieDataState(state: Resource<MovieResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentListMovie?.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
                state.data?.let {
                    loadMovie(it)
                    movieResponse = it
                }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentListMovie?.isRefreshing = false
            }
        }
    }

    private fun loadMovie(movies: MovieResponse) {
        movies.let {
            movieAdapter.clear()
            it.results.let { its -> movieAdapter.fillList(its) }
        }
    }

    private fun loadMoreMovie(movies: MovieResponse) {
        movies.let {
            movieAdapter.clear()
            it.results.let { its -> movieAdapter.fillList(its) }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter.setOnMovieClickListener(this)

        with(binding) {
            linearLayoutManager = LinearLayoutManager(activity)
            movieRv.adapter = movieAdapter
            movieRv.addOnScrollListener(object :  PaginationScrollListener(binding.movieRv.layoutManager as LinearLayoutManager) {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    rowsArrayList.add(null.toString())
                    movieAdapter.notifyItemInserted(rowsArrayList.size);
                    val totalItemCount = movieAdapter.itemCount
                    val lastVisibleItemPosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (rowsArrayList.size >= totalItemCount && lastVisibleItemPosition == rowsArrayList.size) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            movieViewModel.loadMoreMovie(args.id)
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }

                override fun loadMoreItems() {
                    isLoading = true
                    currentPage += 1

                    Handler(Looper.myLooper()!!).postDelayed({
                        loadNextPage()
                        movieViewModel.loadMoreMovie(args.id)
                    }, 1000)
                }

                 override fun getTotalPageCount(): Int {
                    return totalPages
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
        }
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentListMovie.setOnRefreshListener {
            movieViewModel.fetchMovie(args.id)
        }
    }

    override fun onItemClick(genreId: Int) {
        navigate(MovieFragmentDirections.actionMovieFragmentToDetailFragment(genreId))
    }

    fun loadNextPage() {
        movieViewModel.loadMoreMovie(args.id)
    }

}
