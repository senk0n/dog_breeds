package dev.senk0n.dogbreeds.data.breeds.cloud

import dev.senk0n.dogbreeds.data.breeds.cloud.entities.BreedsResponse
import retrofit2.http.GET

interface BreedsSource {
    @GET("breeds/list/all")
    suspend fun getBreeds(): BreedsResponse
}