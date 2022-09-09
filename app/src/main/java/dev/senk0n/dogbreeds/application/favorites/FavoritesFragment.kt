package dev.senk0n.dogbreeds.application.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
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
                is Pending -> {}
                is Empty -> {
                    adapter.submitList(emptyList())
                    binding.favoritesList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_search_off_24)
                    errorBinding.titleText.text = getString(R.string.empty_result)
                }
                is Success -> {
                    adapter.submitList(result.value)
                    errorBinding.errorContainer.visibility = View.GONE
                    binding.favoritesList.visibility = View.VISIBLE
                }
                is Error -> {
                    binding.favoritesList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_error_outline_24)
                    errorBinding.titleText.text =
                        result.cause.message ?: getString(R.string.error_occurred)
                }
            }
        }
        binding.filterFab.setOnClickListener { it.showMenu() }

        binding.favoritesList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoritesList.adapter = adapter

        return binding.root
    }

    private fun deleteFavorite(breedPhoto: BreedPhoto) {
        viewModel.deleteFavorite(breedPhoto)
    }

    private fun View.showMenu() {
        val listPopupWindow = ListPopupWindow(
            requireContext(), null,
            com.google.android.material.R.attr.listPopupWindowStyle
        )
        listPopupWindow.anchorView = this
        listPopupWindow.width = 500

        viewModel.breedsOfFavorites.observe(viewLifecycleOwner) { listOfBreeds ->
            val items: List<Breed> = listOfBreeds.toMutableList().apply { add(Breed("ALL")) }
            val adapter =
                ArrayAdapter(requireContext(), R.layout.list_filter_breed_menu_item, items)
            listPopupWindow.setAdapter(adapter)

            listPopupWindow.setOnItemClickListener { _, _, position, _ ->
                val breed = if (items[position].name == "ALL") null else items[position]
                viewModel.setBreed(breed)
                listPopupWindow.dismiss()
            }
        }
        listPopupWindow.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _errorBinding = null
    }

}