package com.example.movieapp.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.response.GenreResponse
import com.example.movieapp.R
import com.example.movieapp.data.Resource
import com.example.movieapp.databinding.FragmentListGenreBinding
import com.example.movieapp.ui.genre.adapter.GenreAdapter
import com.example.movieapp.ui.genre.viewmodel.GenreViewModel
import com.example.movieapp.util.navigate
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class GenreFragment : Fragment(R.layout.fragment_list_genre), GenreAdapter.OnItemClickListener {

    private val genreViewModel: GenreViewModel by sharedViewModel()
    private var binding: FragmentListGenreBinding? = null
    private val _binding get() = binding!!
    private val genreAdapter: GenreAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListGenreBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        genreViewModel.fetchGenre()
        setupSwipeRefresh()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

        genreViewModel.disposable?.dispose()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            genreViewModel.genreState.collect {
                handleGenreDataState(it)
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            genreViewModel.searchState.collect{

            }
        }
    }

    private fun handleGenreDataState(state: Resource<GenreResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding?.srlFragmentListGenre?.isRefreshing = true

            }

            Resource.Status.SUCCESS -> {
                binding?.srlFragmentListGenre?.isRefreshing = false
                state.data?.let { loadGenre(it) }
            }

            Resource.Status.ERROR -> {
                binding?.srlFragmentListGenre?.isRefreshing = false
            }

            Resource.Status.EMPTY -> {
                binding?.srlFragmentListGenre?.isRefreshing = false
            }
        }
    }

    private fun loadGenre(movies: GenreResponse) {
        movies.let {
            genreAdapter.clear()
            it.genres?.let { it1 -> genreAdapter.fillList(it1) }
        }
    }

    private fun setupRecyclerView() {
        genreAdapter.setOnGenreClickListener(this)

        binding?.rvGenre?.adapter = genreAdapter
    }

    private fun setupSwipeRefresh() {
        binding?.srlFragmentListGenre?.setOnRefreshListener {
            genreViewModel.fetchGenre()
        }
    }

    override fun onItemClick(genreId: Int, genreName: String) {
        navigate(GenreFragmentDirections.actionGenreFragmentToMovieFragment(genreId, genreName))
    }

}