package dev.senk0n.dogbreeds.application.breed_photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.databinding.FragmentBreedPhotoBinding
import dev.senk0n.dogbreeds.shared.core.BreedListItem
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

class BreedPhotoAdapter(
    private val isFavorites: Boolean,
    private val onClick: (breedPhoto: BreedPhoto) -> Unit,
) : ListAdapter<BreedListItem, BreedPhotoAdapter.ViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentBreedPhotoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemPhotoUrl = item.breedPhoto.photoUrl

        with(holder.binding) {
            if (isFavorites) {
                breed.text = item.breedPhoto.breed.toString()
            } else {
                breed.visibility = View.GONE
            }

            cardView.isChecked = item.isFavorite && !isFavorites

            breedImage.load(itemPhotoUrl) {
                placeholder(R.drawable.ic_baseline_image_24)
                error(R.drawable.ic_baseline_hide_image_24)
                scale(Scale.FILL)
            }

            cardView.setOnClickListener {
                cardView.isChecked = !item.isFavorite
                onClick(item.breedPhoto)
            }
        }
    }

    inner class ViewHolder(
        val binding: FragmentBreedPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ItemCallback : DiffUtil.ItemCallback<BreedListItem>() {
        override fun areItemsTheSame(oldItem: BreedListItem, newItem: BreedListItem): Boolean =
            oldItem.breedPhoto.photoUrl == newItem.breedPhoto.photoUrl

        override fun areContentsTheSame(oldItem: BreedListItem, newItem: BreedListItem): Boolean =
            oldItem == newItem
    }
}