package dev.senk0n.dogbreeds.data.breed_photos

import com.squareup.moshi.Moshi
import dev.senk0n.dogbreeds.data.breed_photos.cloud.BreedsPhotosSource
import dev.senk0n.dogbreeds.data.breed_photos.shared.BreedPhotosRepository
import dev.senk0n.dogbreeds.data.http_client.shared.HttpCallWrapper
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedPhotosRepoImpl @Inject constructor(
    moshi: Moshi,
    private val breedsPhotosSource: BreedsPhotosSource,
) : HttpCallWrapper(moshi), BreedPhotosRepository {

    override suspend fun loadBreedPhotos(breed: Breed): List<BreedPhoto> = wrapCallExceptions {
        val response = if (breed.subBreed != null) {
            breedsPhotosSource.getPhotosBySubBreed(breed.name, breed.subBreed)
        } else {
            breedsPhotosSource.getPhotosByBreed(breed.name)
        }
        response.toBreedPhotos()
    }
}