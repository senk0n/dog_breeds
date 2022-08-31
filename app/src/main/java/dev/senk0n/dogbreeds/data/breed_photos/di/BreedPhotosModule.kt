package dev.senk0n.dogbreeds.data.breed_photos.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.data.breed_photos.BreedPhotosRepoImpl
import dev.senk0n.dogbreeds.data.breed_photos.cloud.BreedsPhotosSource
import dev.senk0n.dogbreeds.data.breed_photos.shared.BreedPhotosRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BreedPhotosModule {

    @Provides
    @Singleton
    fun provideBreedPhotosSource(retrofit: Retrofit): BreedsPhotosSource =
        retrofit.create(BreedsPhotosSource::class.java)

    @Provides
    @Singleton
    fun provideBreedPhotosRepository(
        moshi: Moshi,
        breedsPhotosSource: BreedsPhotosSource
    ): BreedPhotosRepository = BreedPhotosRepoImpl(moshi, breedsPhotosSource)

}