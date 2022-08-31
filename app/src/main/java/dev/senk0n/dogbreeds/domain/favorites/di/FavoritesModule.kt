package dev.senk0n.dogbreeds.domain.favorites.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.domain.favorites.FavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUCImpl

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesModule {
    @Binds
    fun bindFavoritesUseCase(
        favoritesUCImpl: FavoritesUCImpl
    ): FavoritesUseCase
}