package dev.senk0n.dogbreeds.domain.breeds

import dev.senk0n.dogbreeds.data.breeds.shared.BreedsRepository
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.shared.core.Breed
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedsUCImpl @Inject constructor(
    private val breedsRepository: BreedsRepository
) : BreedsUseCase {

    override suspend fun getBreeds(): List<Breed> {
        return breedsRepository.loadBreeds()
    }
}