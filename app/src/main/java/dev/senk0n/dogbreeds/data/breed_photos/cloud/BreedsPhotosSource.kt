package dev.senk0n.dogbreeds.data.breed_photos.cloud

import dev.senk0n.dogbreeds.data.breed_photos.cloud.entities.BreedPhotosResponse
import retrofit2.http.GET

interface BreedsPhotosSource {
    @GET("breed/{breed}/{subBreed}/images")
    suspend fun getPhotosBySubBreed(breed: String, subBreed: String): BreedPhotosResponse

    @GET("breed/{breed}/images")
    suspend fun getPhotosByBreed(breed: String): BreedPhotosResponse
}