package dev.senk0n.dogbreeds.domain.edit_favorites.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.domain.edit_favorites.EditFavoritesUCImpl
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase

@Module
@InstallIn(SingletonComponent::class)
interface EditFavoritesModule {
    @Binds
    fun bindEditFavoritesUseCase(
        editFavoritesUCImpl: EditFavoritesUCImpl
    ): EditFavoritesUseCase
}