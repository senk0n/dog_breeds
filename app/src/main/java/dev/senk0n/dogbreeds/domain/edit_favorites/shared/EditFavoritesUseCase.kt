package dev.senk0n.dogbreeds.domain.edit_favorites.shared

import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface EditFavoritesUseCase {
    suspend fun addFavorite(breedPhoto: BreedPhoto)

    suspend fun removeFavorite(breedPhoto: BreedPhoto)

    suspend fun toggleFavorite(breedPhoto: BreedPhoto)
}