package dev.senk0n.dogbreeds.application.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoAdapter
import dev.senk0n.dogbreeds.application.showSnack
import dev.senk0n.dogbreeds.databinding.FragmentFavoritesListBinding
import dev.senk0n.dogbreeds.databinding.PartErrorBinding
import dev.senk0n.dogbreeds.shared.core.*

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesListBinding? = null
    private val binding get() = _binding!!
    private var _errorBinding: PartErrorBinding? = null
    private val errorBinding get() = _errorBinding!!
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter: BreedPhotoAdapter = BreedPhotoAdapter(true, ::deleteFavorite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        _errorBinding = PartErrorBinding.bind(binding.root)

        viewModel.snack.observe(viewLifecycleOwner) { showSnack(it) }
        viewModel.favorites.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Pending -> {
//                    binding.swipeRefresh.isRefreshing = true
                }
                is Empty -> {
                    adapter.list = emptyList()
                    binding.favoritesList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_search_off_24)
                    errorBinding.titleText.text = getString(R.string.empty_result)
//                    binding.swipeRefresh.isRefreshing = false
                }
                is Success -> {
                    adapter.list = result.value
                    errorBinding.errorContainer.visibility = View.GONE
                    binding.favoritesList.visibility = View.VISIBLE
//                    binding.swipeRefresh.isRefreshing = false
                }
                is Error -> {
                    binding.favoritesList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_error_outline_24)
                    errorBinding.titleText.text =
                        result.cause.message ?: getString(R.string.error_occurred)
//                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        binding.favoritesList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.favoritesList.adapter = adapter

        return binding.root
    }

    private fun deleteFavorite(breedPhoto: BreedPhoto) {
        viewModel.deleteFavorite(breedPhoto)
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _errorBinding = null
    }

}