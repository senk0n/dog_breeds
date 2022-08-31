package dev.senk0n.dogbreeds.domain.favorites

import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface FavoritesUseCase {
    suspend fun getFavorites(breed: Breed): List<BreedPhoto>
}