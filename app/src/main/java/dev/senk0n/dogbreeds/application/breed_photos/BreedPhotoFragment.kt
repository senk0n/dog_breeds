package dev.senk0n.dogbreeds.application.breed_photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.showSnack
import dev.senk0n.dogbreeds.databinding.FragmentBreedPhotoListBinding
import dev.senk0n.dogbreeds.databinding.PartErrorBinding
import dev.senk0n.dogbreeds.shared.core.*

class BreedPhotoFragment : Fragment() {
    private var _binding: FragmentBreedPhotoListBinding? = null
    private val binding get() = _binding!!
    private var _errorBinding: PartErrorBinding? = null
    private val errorBinding get() = _errorBinding!!
    private val viewModel: BreedPhotoViewModel by viewModels()
    private val adapter: BreedPhotoAdapter = BreedPhotoAdapter(::toggleBreedFavorite)

    companion object {
        const val ARG_BREED = "BREED"
        const val ARG_SUB_BREED = "SUB_BREED"

        fun newInstance(breed: String, subBreed: String?) =
            BreedPhotoFragment().apply {
                arguments = bundleOf(
                    ARG_BREED to breed,
                    ARG_SUB_BREED to (subBreed ?: ""),
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val breed = it.getString(ARG_BREED)
            val subBreed = it.getString(ARG_SUB_BREED)
            if (breed != null) {
                viewModel.setBreed(Breed(breed, subBreed))
            } else viewModel.refresh()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedPhotoListBinding.inflate(inflater, container, false)
        _errorBinding = PartErrorBinding.bind(binding.root)

        viewModel.snack.observe(viewLifecycleOwner) { showSnack(it) }
        viewModel.breedPhotos.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Pending -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is Empty -> {
                    adapter.list = emptyList()
                    binding.breedPhotoList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_search_off_24)
                    errorBinding.titleText.text = getString(R.string.empty_result)
                    binding.swipeRefresh.isRefreshing = false
                }
                is Success -> {
                    adapter.list = result.value
                    errorBinding.errorContainer.visibility = View.GONE
                    binding.breedPhotoList.visibility = View.VISIBLE
                    binding.swipeRefresh.isRefreshing = false
                }
                is Error -> {
                    binding.breedPhotoList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_error_outline_24)
                    errorBinding.titleText.text =
                        result.cause.message ?: getString(R.string.error_occurred)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        binding.breedPhotoList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.breedPhotoList.adapter = adapter

        return binding.root
    }

    private fun toggleBreedFavorite(photoUrl: String) =
        viewModel.toggleBreedFavorite(photoUrl)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _errorBinding = null
    }
}