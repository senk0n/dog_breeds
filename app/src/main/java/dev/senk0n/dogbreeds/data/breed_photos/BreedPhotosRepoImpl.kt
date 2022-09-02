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
        if (breed.name == "meme") return@wrapCallExceptions buildList {
            listOf(
                "1mEiS9E", "n463u4a", "yd7oFj6", "1gHSL1D", "V3KX3Ww", "YTf254y", "vCRL9A8",
                "iFJCzAa", "Xw1tynA", "98bEAHA", "9E5s3bu",
            ).forEach { add(BreedPhoto(Breed("meme"), "https://i.imgur.com/$it.png")) }
        }
        val response = if (breed.subBreed != null) {
            breedsPhotosSource.getPhotosBySubBreed(breed.name, breed.subBreed)
        } else {
            breedsPhotosSource.getPhotosByBreed(breed.name)
        }
        response.toBreedPhotos()
    }

    override suspend fun loadRandomBreedPhoto(breed: Breed): BreedPhoto = wrapCallExceptions {
        val response = if (breed.subBreed != null) {
            breedsPhotosSource.getOnePhotoBySubBreed(breed.name, breed.subBreed)
        } else {
            breedsPhotosSource.getOnePhotoByBreed(breed.name)
        }
        response.toBreedPhoto()
    }
}