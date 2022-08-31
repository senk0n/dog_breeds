package dev.senk0n.dogbreeds.domain.edit_favorites

import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditFavoritesUCImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : EditFavoritesUseCase {
    override suspend fun addFavorite(breedPhoto: BreedPhoto) {
        favoritesRepository.addFavorite(breedPhoto)
    }

    override suspend fun removeFavorite(breedPhoto: BreedPhoto) {
        favoritesRepository.removeFavorite(breedPhoto)
    }

    override suspend fun toggleFavorite(breedPhoto: BreedPhoto) {
        val isFav = favoritesRepository.isFavorite(breedPhoto)

        if (isFav) {
            favoritesRepository.removeFavorite(breedPhoto)
        } else {
            favoritesRepository.addFavorite(breedPhoto)
        }
    }

}