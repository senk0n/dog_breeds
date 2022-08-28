package dev.senk0n.dogbreeds.data.favorites

import dev.senk0n.dogbreeds.data.favorites.local.FavoritesDataSource
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoriteUrlTuple
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoritesEntity
import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

class FavoriteRepoImpl(
    private val favoritesDataSource: FavoritesDataSource,
) : FavoritesRepository {
    override suspend fun loadFavoritesByBreed(breed: Breed): List<BreedPhoto> =
        if (breed.subBreed == null) {
            favoritesDataSource.getByBreed(breed.name)
        } else {
            favoritesDataSource.getBySubBreed(breed.name, breed.subBreed)
        }.map { it.toBreedPhoto() }

    override suspend fun addFavorite(breedPhoto: BreedPhoto) {
        favoritesDataSource.create(
            FavoritesEntity.fromBreedPhoto(breedPhoto)
        )
    }

    override suspend fun removeFavorite(breedPhoto: BreedPhoto) {
        favoritesDataSource.removeByUrl(FavoriteUrlTuple(breedPhoto.photoUrl))
    }
}