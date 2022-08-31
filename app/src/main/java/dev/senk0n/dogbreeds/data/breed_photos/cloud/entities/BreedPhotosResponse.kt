package dev.senk0n.dogbreeds.data.breed_photos.cloud.entities

import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import dev.senk0n.dogbreeds.shared.core.toBreed

data class BreedPhotosResponse(
    val message: List<String>,
    val status: String,
) {
    fun toBreedPhotos(): List<BreedPhoto> = message.map { url ->
        BreedPhoto(url.toBreed(), url)
    }
}
