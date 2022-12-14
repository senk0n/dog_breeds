package dev.senk0n.dogbreeds.data.favorites.shared

import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

interface FavoritesRepository {
    suspend fun loadFavorites(): List<BreedPhoto>

    suspend fun loadFavoritesByBreed(breed: Breed): List<BreedPhoto>

    suspend fun addFavorite(breedPhoto: BreedPhoto)

    suspend fun removeFavorite(breedPhoto: BreedPhoto)

    suspend fun isFavorite(breedPhoto: BreedPhoto): Boolean

    suspend fun loadBreeds(): List<Breed>
}