package dev.senk0n.dogbreeds.application.breeds_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoFragment.Companion.ARG_BREED
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoFragment.Companion.ARG_SUB_BREED
import dev.senk0n.dogbreeds.application.shared.BaseFragment
import dev.senk0n.dogbreeds.databinding.FragmentBreedsListBinding
import dev.senk0n.dogbreeds.shared.core.*

@AndroidEntryPoint
class BreedsFragment : BaseFragment() {
    private var _binding: FragmentBreedsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BreedsViewModel by viewModels()
    private val adapter: BreedsAdapter = BreedsAdapter(::openBreedDetails)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBreedsListBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { result ->
                when (result) {
                    is Pending -> {
                        activityBinding.swipeRefresh.isRefreshing = true
                    }
                    is Empty -> {
                        adapter.submitList(emptyList())
                        binding.breedsList.visibility = View.GONE
                        showError(title = getString(R.string.empty_result), imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_search_off_24)
                        }) { viewModel.refresh() }
                        activityBinding.swipeRefresh.isRefreshing = false
                    }
                    is Success -> {
                        adapter.submitList(result.value)
                        hideError()
                        binding.breedsList.visibility = View.VISIBLE
                        activityBinding.swipeRefresh.isRefreshing = false
                    }
                    is Error -> {
                        binding.breedsList.visibility = View.GONE
                        showError(title = result.cause.message, imageSetter = {
                            it.setImageResource(R.drawable.ic_baseline_error_outline_24)
                        }) { viewModel.refresh() }
                        activityBinding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }

        activityBinding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        binding.breedsList.layoutManager = LinearLayoutManager(requireContext())
        binding.breedsList.adapter = adapter

        with(MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)) {
            isLastItemDecorated = false
            dividerInsetStart = 120
            binding.breedsList.addItemDecoration(this)
        }

        return binding.root
    }

    private fun openBreedDetails(breed: Breed) {
        findNavController().navigate(
            R.id.action_breedsFragment_to_breedPhotoFragment,
            bundleOf(
                ARG_BREED to breed.name,
                ARG_SUB_BREED to (breed.subBreed ?: ""),
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}