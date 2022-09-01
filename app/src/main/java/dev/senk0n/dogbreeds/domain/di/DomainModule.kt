package dev.senk0n.dogbreeds.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Named("Default")
    @Provides
    fun provideDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    @Named("IO")
    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}