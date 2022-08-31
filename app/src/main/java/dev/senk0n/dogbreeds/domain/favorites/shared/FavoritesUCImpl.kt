package dev.senk0n.dogbreeds.domain.favorites.shared

import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import dev.senk0n.dogbreeds.domain.favorites.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesUCImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : FavoritesUseCase {

    override suspend fun getFavorites(breed: Breed): List<BreedPhoto> {
        return favoritesRepository.loadFavoritesByBreed(breed)
    }
}