package dev.senk0n.dogbreeds.domain.breeds

import dev.senk0n.dogbreeds.data.breed_photos.shared.BreedPhotosRepository
import dev.senk0n.dogbreeds.data.breeds.shared.BreedsRepository
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BreedsUCImpl @Inject constructor(
    private val breedsRepository: BreedsRepository,
    private val breedPhotosRepository: BreedPhotosRepository,
    @Named("Default") private val defaultDispatcher: CoroutineDispatcher,
) : BreedsUseCase {

    override suspend fun getBreeds(): List<BreedPhoto> = withContext(defaultDispatcher) {
        val breeds = breedsRepository.loadBreeds()

        breeds.map { breed ->
            async { breedPhotosRepository.loadRandomBreedPhoto(breed) }
        }.map { it.await() }
    }
}