package dev.senk0n.dogbreeds.domain.breeds.shared

import dev.senk0n.dogbreeds.shared.core.Breed

interface BreedsUseCase {
    suspend fun getBreeds(): List<Breed>
}