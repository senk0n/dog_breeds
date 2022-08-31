package dev.senk0n.dogbreeds.data.breeds.cloud.entities

import dev.senk0n.dogbreeds.shared.core.Breed

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String,
) {
    fun toBreeds(): List<Breed> = message.flatMap { (breed, subBreeds) ->
        buildList {
            subBreeds.forEach { subBreed ->
                Breed(breed, subBreed)
            }
        }
    }
}
