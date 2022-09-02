package dev.senk0n.dogbreeds.domain.breeds.shared

import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface BreedsUseCase {
    suspend fun getBreeds(): List<BreedPhoto>
}