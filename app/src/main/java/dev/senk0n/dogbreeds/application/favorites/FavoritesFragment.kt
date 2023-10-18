package dev.senk0n.dogbreeds.application.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoAdapter
import dev.senk0n.dogbreeds.application.shared.BaseFragment
import dev.senk0n.dogbreeds.databinding.FragmentFavoritesListBinding
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment() {
    private var _binding: FragmentFavoritesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter: BreedPhotoAdapter = BreedPhotoAdapter(true, ::deleteFavorite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)

        activityBinding.swipeRefresh.isEnabled = false
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { result ->
                when (result) {
                    is Pending -> {}
                    is Empty -> {
                        adapter.submitList(emptyList())
                        binding.favoritesList.visibility = View.GONE
                        showError(title = getString(R.string.empty_result), imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_search_off_24)
                        }) { viewModel.refresh() }
                    }
                    is Success -> {
                        adapter.submitList(result.value)
                        hideError()
                        binding.favoritesList.visibility = View.VISIBLE
                    }
                    is Error -> {
                        binding.favoritesList.visibility = View.GONE
                        showError(title = result.cause.message, imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_error_outline_24)
                        }) { viewModel.refresh() }
                    }
                }
            }
        }

        val listPopupWindow = binding.filterFab.setupMenu()
        binding.filterFab.setOnClickListener { listPopupWindow.show() }

        binding.favoritesList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoritesList.adapter = adapter

        return binding.root
    }

    private fun deleteFavorite(breedPhoto: BreedPhoto) {
        viewModel.deleteFavorite(breedPhoto)
    }

    private fun View.setupMenu(): ListPopupWindow {
        val listPopupWindow = ListPopupWindow(
            requireContext(), null,
            com.google.android.material.R.attr.listPopupWindowStyle
        )
        listPopupWindow.anchorView = this
        listPopupWindow.width = 500

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.breedsOfFavorites.collect { listOfBreeds ->
                    val items: List<Breed> =
                        listOfBreeds.toMutableList().apply { add(Breed("ALL")) }
                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.list_filter_breed_menu_item, items)
                    listPopupWindow.setAdapter(adapter)

                    listPopupWindow.setOnItemClickListener { _, _, position, _ ->
                        val breed = if (items[position].name == "ALL") null else items[position]
                        viewModel.setBreed(breed)
                        listPopupWindow.dismiss()
                    }
                }
            }
        }

        return listPopupWindow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}