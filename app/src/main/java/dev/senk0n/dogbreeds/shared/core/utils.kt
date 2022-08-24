package dev.senk0n.dogbreeds.shared.core

fun String.toBreed(): Breed {
    val breedTxt = this.split('/')[4]

    return if (breedTxt.contains('-')) {
        val breedParts = breedTxt.split('-')
        Breed(breedParts[0], breedParts[1])
    } else Breed(breedTxt)
}
