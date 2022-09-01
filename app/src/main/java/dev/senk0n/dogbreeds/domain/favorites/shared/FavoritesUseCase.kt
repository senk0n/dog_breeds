package dev.senk0n.dogbreeds.domain.favorites.shared

import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface FavoritesUseCase {
    suspend fun getFavorites(): List<BreedPhoto>

    suspend fun getFavoritesByBreed(breed: Breed): List<BreedPhoto>
}