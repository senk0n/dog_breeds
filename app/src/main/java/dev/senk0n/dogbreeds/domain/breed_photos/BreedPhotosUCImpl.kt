package dev.senk0n.dogbreeds.domain.breed_photos

import dev.senk0n.dogbreeds.data.breed_photos.shared.BreedPhotosRepository
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BreedPhotosUCImpl @Inject constructor(
    private val breedPhotosRepository: BreedPhotosRepository,
    @Named("Default") private val defaultDispatcher: CoroutineDispatcher,
) : BreedPhotosUseCase {

    override suspend fun loadPhotos(breed: Breed): List<BreedPhoto> =
        withContext(defaultDispatcher) {
            breedPhotosRepository.loadBreedPhotos(breed)
        }

    override suspend fun loadPhotos(breeds: List<Breed>): Flow<BreedPhoto> = flow {
        breeds.map { breed ->
            emit(breedPhotosRepository.loadRandomBreedPhoto(breed))
        }
    }.flowOn(defaultDispatcher)

}