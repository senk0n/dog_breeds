package dev.senk0n.dogbreeds.domain.breeds.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.domain.breeds.BreedsUCImpl
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase

@Module
@InstallIn(SingletonComponent::class)
interface BreedsModule {
    @Binds
    fun bindBreedsUseCase(
        breedsUCImpl: BreedsUCImpl
    ): BreedsUseCase
}