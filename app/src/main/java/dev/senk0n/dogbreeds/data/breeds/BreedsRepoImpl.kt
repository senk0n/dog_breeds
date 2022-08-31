package dev.senk0n.dogbreeds.data.breeds

import com.squareup.moshi.Moshi
import dev.senk0n.dogbreeds.data.breeds.cloud.BreedsSource
import dev.senk0n.dogbreeds.data.breeds.shared.BreedsRepository
import dev.senk0n.dogbreeds.data.http_client.shared.HttpCallWrapper
import dev.senk0n.dogbreeds.shared.core.Breed
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedsRepoImpl @Inject constructor(
    moshi: Moshi,
    private val breedsSource: BreedsSource,
) : HttpCallWrapper(moshi), BreedsRepository {

    override suspend fun loadBreeds(): List<Breed> = wrapCallExceptions {
        breedsSource.getBreeds().toBreeds()
    }
}
