package dev.senk0n.dogbreeds.data.favorites.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.data.favorites.FavoritesRepoImpl
import dev.senk0n.dogbreeds.data.favorites.local.AppDataBase
import dev.senk0n.dogbreeds.data.favorites.local.FavoritesSource
import dev.senk0n.dogbreeds.data.favorites.shared.FavoritesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavoritesModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, "database.db")
            .build()

    @Provides
    @Singleton
    fun provideFavoritesSource(appDataBase: AppDataBase): FavoritesSource =
        appDataBase.getFavoritesDao()

    @Provides
    fun provideFavoritesRepository(favoritesSource: FavoritesSource): FavoritesRepository =
        FavoritesRepoImpl(favoritesSource)
}