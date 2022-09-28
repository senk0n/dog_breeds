package dev.senk0n.dogbreeds.application.breed_photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.shared.BaseFragment
import dev.senk0n.dogbreeds.databinding.FragmentBreedPhotoListBinding
import dev.senk0n.dogbreeds.shared.core.*

@AndroidEntryPoint
class BreedPhotoFragment : BaseFragment() {
    private var _binding: FragmentBreedPhotoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BreedPhotoViewModel by viewModels()
    private val adapter: BreedPhotoAdapter = BreedPhotoAdapter(false, ::toggleBreedFavorite)

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
            var subBreed = it.getString(ARG_SUB_BREED)
            if (breed != null) {
                if (subBreed?.isEmpty() == true) subBreed = null
                viewModel.setBreed(Breed(breed, subBreed))
            } else viewModel.refresh()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBreedPhotoListBinding.inflate(inflater, container, false)

        activityBinding.swipeRefresh.isEnabled = false
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { result ->
                when (result) {
                    is Pending -> {
                        activityBinding.progressBar.visibility = View.VISIBLE
                    }
                    is Empty -> {
                        adapter.submitList(emptyList())
                        binding.breedPhotoList.visibility = View.GONE
                        showError(title = getString(R.string.empty_result), imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_search_off_24)
                        }) { viewModel.refresh() }
                        activityBinding.progressBar.visibility = View.GONE
                    }
                    is Success -> {
                        adapter.submitList(result.value)
                        hideError()
                        binding.breedPhotoList.visibility = View.VISIBLE
                        activityBinding.progressBar.visibility = View.GONE
                    }
                    is Error -> {
                        binding.breedPhotoList.visibility = View.GONE
                        showError(title = result.cause.message, imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_error_outline_24)
                        }) { viewModel.refresh() }
                        activityBinding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.breedPhotoList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.breedPhotoList.adapter = adapter
        val itemAnimator = binding.breedPhotoList.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        return binding.root
    }

    private fun toggleBreedFavorite(breedPhoto: BreedPhoto) {
        viewModel.toggleBreedFavorite(breedPhoto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}