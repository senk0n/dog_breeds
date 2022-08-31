package dev.senk0n.dogbreeds.domain.breed_photos.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.domain.breed_photos.BreedPhotosUCImpl
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase

@Module
@InstallIn(SingletonComponent::class)
interface BreedPhotosModule {
    @Binds
    fun bindBreedPhotosUseCase(
        breedPhotosUCImpl: BreedPhotosUCImpl
    ): BreedPhotosUseCase
}