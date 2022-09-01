package dev.senk0n.dogbreeds.shared.core

data class Breed(val name: String, val subBreed: String? = null) {
    override fun toString(): String {
        return if (subBreed == null) name
        else "$subBreed $name"
    }
}

data class BreedPhoto(val breed: Breed, val photoUrl: String)

data class BreedListItem(val breedPhoto: BreedPhoto, val isFavorite: Boolean = false)
