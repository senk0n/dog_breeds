package dev.senk0n.dogbreeds.application.breed_photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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
) : RecyclerView.Adapter<BreedPhotoAdapter.ViewHolder>() {
    var list: List<BreedListItem> = emptyList()
        set(value) {
            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = field.size
                override fun getNewListSize(): Int = value.size
                override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
                    field[oldItem].breedPhoto.photoUrl == value[newItem].breedPhoto.photoUrl

                override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
                    field[oldItem] == value[newItem]
            }
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentBreedPhotoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val itemPhotoUrl = item.breedPhoto.photoUrl

        with(holder.binding) {
            if (isFavorites) {
                breed.text = item.breedPhoto.breed.name
                subBreed.text = item.breedPhoto.breed.subBreed ?: ""
            } else {
                breed.visibility = View.GONE
                subBreed.visibility = View.GONE
            }

            if (item.isFavorite && !isFavorites) {
                favoriteMark.visibility = View.VISIBLE
            } else favoriteMark.visibility = View.GONE

            breedImage.load(itemPhotoUrl) {
                placeholder(R.drawable.ic_baseline_image_24)
                error(R.drawable.ic_baseline_hide_image_24)
                scale(Scale.FILL)
            }

            root.setOnClickListener {
                if (item.isFavorite) {
                    favoriteMark.visibility = View.GONE
                } else favoriteMark.visibility = View.VISIBLE

                onClick(item.breedPhoto)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        val binding: FragmentBreedPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root)

}