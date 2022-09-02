package dev.senk0n.dogbreeds.application.breeds_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoFragment.Companion.ARG_BREED
import dev.senk0n.dogbreeds.application.breed_photos.BreedPhotoFragment.Companion.ARG_SUB_BREED
import dev.senk0n.dogbreeds.application.showSnack
import dev.senk0n.dogbreeds.databinding.FragmentBreedsListBinding
import dev.senk0n.dogbreeds.databinding.PartErrorBinding
import dev.senk0n.dogbreeds.shared.core.*

@AndroidEntryPoint
class BreedsFragment : Fragment() {
    private var _binding: FragmentBreedsListBinding? = null
    private val binding get() = _binding!!
    private var _errorBinding: PartErrorBinding? = null
    private val errorBinding get() = _errorBinding!!
    private val viewModel: BreedsViewModel by viewModels()
    private val adapter: BreedsAdapter = BreedsAdapter(::openBreedDetails)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsListBinding.inflate(inflater, container, false)
        _errorBinding = PartErrorBinding.bind(binding.root)

        viewModel.snack.observe(viewLifecycleOwner) { showSnack(it) }
        viewModel.breeds.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Pending -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is Empty -> {
                    adapter.list = emptyList()
                    binding.breedsList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_search_off_24)
                    errorBinding.titleText.text = getString(R.string.empty_result)
                    binding.swipeRefresh.isRefreshing = false
                }
                is Success -> {
                    adapter.list = result.value
                    errorBinding.errorContainer.visibility = View.GONE
                    binding.breedsList.visibility = View.VISIBLE
                    binding.swipeRefresh.isRefreshing = false
                }
                is Error -> {
                    binding.breedsList.visibility = View.GONE
                    errorBinding.errorContainer.visibility = View.VISIBLE
                    errorBinding.errorImage.setImageResource(R.drawable.ic_baseline_error_outline_24)
                    errorBinding.titleText.text =
                        result.cause.message ?: getString(R.string.error_occurred)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.loadBreeds() }

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
        _errorBinding = null
    }
}