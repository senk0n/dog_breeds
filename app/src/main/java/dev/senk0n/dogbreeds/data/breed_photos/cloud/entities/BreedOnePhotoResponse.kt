package dev.senk0n.dogbreeds.data.breed_photos.cloud.entities

import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import dev.senk0n.dogbreeds.shared.core.toBreed

data class BreedOnePhotoResponse(
    val message: String,
    val status: String,
) {
    fun toBreedPhoto(): BreedPhoto = BreedPhoto(message.toBreed(), message)
}
