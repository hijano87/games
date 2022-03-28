package com.hijano.games.di

import com.hijano.games.api.GamesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
internal object ServiceModule {
    @Provides
    fun provideGamesService(retrofit: Retrofit): GamesService {
        return retrofit.create(GamesService::class.java)
    }
}
