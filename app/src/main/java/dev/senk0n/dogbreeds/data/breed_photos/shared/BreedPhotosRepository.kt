package dev.senk0n.dogbreeds.data.breed_photos.shared

import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface BreedPhotosRepository {
    suspend fun loadBreedPhotos(breed: Breed): List<BreedPhoto>

    suspend fun loadRandomBreedPhoto(breed: Breed): BreedPhoto
}