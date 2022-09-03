package dev.senk0n.dogbreeds.domain.breed_photos.shared

import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface BreedPhotosUseCase {
    suspend fun loadPhotos(breed: Breed): List<BreedPhoto>

    suspend fun loadPhotos(breeds: List<Breed>): List<BreedPhoto>
}