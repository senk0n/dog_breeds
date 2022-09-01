package dev.senk0n.dogbreeds.data.favorites

import dev.senk0n.dogbreeds.data.favorites.local.FavoritesSource
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoriteUrlTuple
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoritesEntity
import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepoImpl @Inject constructor(
    private val favoritesSource: FavoritesSource,
) : FavoritesRepository {
    override suspend fun loadFavorites(): List<BreedPhoto> =
        favoritesSource.getFavorites().map { it.toBreedPhoto() }

    override suspend fun loadFavoritesByBreed(breed: Breed): List<BreedPhoto> =
        if (breed.subBreed == null) {
            favoritesSource.getByBreed(breed.name)
        } else {
            favoritesSource.getBySubBreed(breed.name, breed.subBreed)
        }.map { it.toBreedPhoto() }

    override suspend fun addFavorite(breedPhoto: BreedPhoto) {
        favoritesSource.create(
            FavoritesEntity.fromBreedPhoto(breedPhoto)
        )
    }

    override suspend fun removeFavorite(breedPhoto: BreedPhoto) {
        favoritesSource.removeByUrl(FavoriteUrlTuple(breedPhoto.photoUrl))
    }

    override suspend fun isFavorite(breedPhoto: BreedPhoto): Boolean =
        favoritesSource.isExistsByUrl(breedPhoto.photoUrl)

    override suspend fun loadBreeds(): List<Breed> =
        favoritesSource.getBreeds().map { Breed(it.breed, it.subBreed) }

}