package com.hijano.games.di

import android.content.Context
import androidx.room.Room
import com.hijano.games.db.GamesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesGamesDatabase(@ApplicationContext appContext: Context): GamesDatabase {
        return Room.databaseBuilder(
            appContext,
            GamesDatabase::class.java,
            "GamesDatabase.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
