package com.example.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.Constants
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.room.FavoriteMovie
import com.example.movieapp.view.adapters.FavoriteMovieAdapter
import com.example.movieapp.viewModel.FavoriteMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private val viewModel by viewModels<FavoriteMovieViewModel>()
    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set toolbar text as "Favorites"
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"
        // Enable the back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = FavoriteMovieAdapter(
            object : FavoriteMovieAdapter.OnItemClickListener {
                override fun onItemClick(movie: FavoriteMovie) {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                        Constants.POPULAR,
                        movie.id!!,
                    )
                    findNavController().navigate(action)
                }
            },
        )
        with(binding.favoriteRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }
        viewModel.favMovieList.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            val isListEmpty = it.isEmpty()
            binding.emptyFavListImage.visibility = if (isListEmpty) View.VISIBLE else View.GONE
            binding.emptyFavListText.visibility = if (isListEmpty) View.VISIBLE else View.GONE
            binding.favoriteRecyclerView.visibility = if (isListEmpty) View.GONE else View.VISIBLE
        }
    }
}
