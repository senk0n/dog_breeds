package dev.senk0n.dogbreeds.data.breeds.shared

import dev.senk0n.dogbreeds.shared.core.Breed

interface BreedsRepository {
    suspend fun loadBreeds(): List<Breed>
}