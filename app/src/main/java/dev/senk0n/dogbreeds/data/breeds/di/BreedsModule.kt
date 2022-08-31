package dev.senk0n.dogbreeds.data.breeds.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.senk0n.dogbreeds.data.breeds.BreedsRepoImpl
import dev.senk0n.dogbreeds.data.breeds.cloud.BreedsSource
import dev.senk0n.dogbreeds.data.breeds.shared.BreedsRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BreedsModule {

    @Provides
    @Singleton
    fun provideBreedsSource(retrofit: Retrofit): BreedsSource =
        retrofit.create(BreedsSource::class.java)

    @Provides
    @Singleton
    fun provideBreedsRepository(
        moshi: Moshi, breedsSource: BreedsSource
    ): BreedsRepository = BreedsRepoImpl(moshi, breedsSource)

}