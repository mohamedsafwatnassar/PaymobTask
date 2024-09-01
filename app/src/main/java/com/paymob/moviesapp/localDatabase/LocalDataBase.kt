package com.paymob.moviesapp.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters
@Database(entities = [], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun moviesListDao(): MoviesListDao

    companion object {
        private var instance: LocalDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): LocalDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    LocalDataBase::class.java,
                    "news_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}
