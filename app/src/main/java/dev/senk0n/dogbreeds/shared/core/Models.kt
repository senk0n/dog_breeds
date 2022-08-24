package dev.senk0n.dogbreeds.shared.core

data class Breed(val name: String, val subBreed: String? = null)

data class BreedPhoto(val breed: Breed, val photoUrl: String)

data class BreedListItem(val breedPhoto: BreedPhoto, val isFavorite: Boolean = false)
