package dev.senk0n.dogbreeds.application.breeds_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.senk0n.dogbreeds.databinding.FragmentBreedsBinding
import dev.senk0n.dogbreeds.shared.core.Breed

class BreedsAdapter(
    private val onClick: (breed: Breed) -> Unit,
) : RecyclerView.Adapter<BreedsAdapter.ViewHolder>() {
    var list: List<Breed> = emptyList()
        set(value) {
            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = field.size
                override fun getNewListSize(): Int = value.size
                override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
                    field[oldItem].name == value[newItem].name

                override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
                    field[oldItem] == value[newItem]
            }
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentBreedsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val breedItem = list[position]
        with(holder.binding) {
            breed.text = breedItem.name
            subBreed.text = breedItem.subBreed
            if (breedItem.subBreed != null) {
                subBreed.visibility = View.VISIBLE
            } else {
                subBreed.visibility = View.GONE
            }

            root.setOnClickListener { onClick(Breed(breedItem.name, breedItem.subBreed)) }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        val binding: FragmentBreedsBinding
    ) : RecyclerView.ViewHolder(binding.root)

}
