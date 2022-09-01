package dev.senk0n.dogbreeds.data.breeds.cloud.entities

import dev.senk0n.dogbreeds.shared.core.Breed

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String,
) {
    fun toBreeds(): List<Breed> = message.flatMap { (breed, subBreeds) ->
        buildList {
            if (subBreeds.isEmpty()) {
                add(Breed(breed))
            } else {
                subBreeds.forEach { subBreed ->
                    add(Breed(breed, subBreed))
                }
            }
        }
    }
}
