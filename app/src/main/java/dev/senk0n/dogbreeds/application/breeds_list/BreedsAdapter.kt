package dev.senk0n.dogbreeds.application.breeds_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.databinding.FragmentBreedsBinding
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

class BreedsAdapter(
    private val onClick: (breed: Breed) -> Unit,
) : ListAdapter<BreedPhoto, BreedsAdapter.ViewHolder>(ItemCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            breed.text = item.breed.name
            subBreed.text = item.breed.subBreed
            if (item.breed.subBreed != null) {
                subBreed.visibility = View.VISIBLE
            } else {
                subBreed.visibility = View.GONE
            }

            if (item.photoUrl.isNotBlank())
                breedImage.load(item.photoUrl) {
                    placeholder(R.drawable.ic_baseline_image_24)
                    error(R.drawable.ic_baseline_hide_image_24)
                    transformations(CircleCropTransformation())
                    scale(Scale.FILL)
                }

            root.setOnClickListener { onClick(Breed(item.breed.name, item.breed.subBreed)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentBreedsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class ViewHolder(
        val binding: FragmentBreedsBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ItemCallback : DiffUtil.ItemCallback<BreedPhoto>() {
        override fun areItemsTheSame(oldItem: BreedPhoto, newItem: BreedPhoto): Boolean =
            oldItem.breed.name == newItem.breed.name

        override fun areContentsTheSame(oldItem: BreedPhoto, newItem: BreedPhoto): Boolean =
            oldItem == newItem
    }
}
