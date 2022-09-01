package dev.senk0n.dogbreeds.domain.favorites

import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FavoritesUCImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    @Named("Default") private val defaultDispatcher: CoroutineDispatcher,
) : FavoritesUseCase {
    override suspend fun getFavorites(): List<BreedPhoto> = withContext(defaultDispatcher) {
        favoritesRepository.loadFavorites()
    }

    override suspend fun getFavoritesByBreed(breed: Breed): List<BreedPhoto> =
        withContext(defaultDispatcher) {
            favoritesRepository.loadFavoritesByBreed(breed)
        }

    override suspend fun getBreedsOfFavorites(): List<Breed> = withContext(defaultDispatcher) {
        favoritesRepository.loadBreeds()
    }
}