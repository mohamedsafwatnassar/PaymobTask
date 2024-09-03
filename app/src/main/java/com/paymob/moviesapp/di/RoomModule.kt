package com.paymob.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.paymob.moviesapp.localDatabase.FavoriteDao
import com.paymob.moviesapp.localDatabase.LocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): LocalDataBase {
        return Room.databaseBuilder(
            appContext, LocalDataBase::class.java, "movie_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideMovieDao(db: LocalDataBase): FavoriteDao {
        return db.moviesListDao()
    }
}