package com.paymob.moviesapp.di

import android.content.Context
import com.paymob.moviesapp.localDatabase.LocalDataBase
import com.paymob.moviesapp.localDatabase.MoviesListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideNewsDao(@ApplicationContext context: Context): MoviesListDao {
        return LocalDataBase.getInstance(context).moviesListDao()
    }
}