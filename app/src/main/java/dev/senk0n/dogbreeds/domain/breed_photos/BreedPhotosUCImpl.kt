package dev.senk0n.dogbreeds.domain.breed_photos

import dev.senk0n.dogbreeds.data.breed_photos.shared.BreedPhotosRepository
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedPhotosUCImpl @Inject constructor(
    private val breedPhotosRepository: BreedPhotosRepository
) : BreedPhotosUseCase {

    override suspend fun loadPhotos(breed: Breed): List<BreedPhoto> {
        return breedPhotosRepository.loadBreedPhotos(breed)
    }

}