package dev.senk0n.dogbreeds.data.breed_photos.cloud

import dev.senk0n.dogbreeds.data.breed_photos.cloud.entities.BreedOnePhotoResponse
import dev.senk0n.dogbreeds.data.breed_photos.cloud.entities.BreedPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedsPhotosSource {
    @GET("breed/{breed}/{sub_reed}/images")
    suspend fun getPhotosBySubBreed(
        @Path("breed") breed: String,
        @Path("sub_reed") subBreed: String
    ): BreedPhotosResponse

    @GET("breed/{breed}/images")
    suspend fun getPhotosByBreed(@Path("breed") breed: String): BreedPhotosResponse

    @GET("breed/{breed}/{sub_reed}/images/random")
    suspend fun getOnePhotoBySubBreed(
        @Path("breed") breed: String,
        @Path("sub_reed") subBreed: String
    ): BreedOnePhotoResponse

    @GET("breed/{breed}/images/random")
    suspend fun getOnePhotoByBreed(@Path("breed") breed: String): BreedOnePhotoResponse
}