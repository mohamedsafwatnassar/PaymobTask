package com.paymob.moviesapp.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paymob.moviesapp.model.FavoriteModel
import com.paymob.moviesapp.model.MovieItem

@Database(entities = [FavoriteModel::class], version = 7, exportSchema = false)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun moviesListDao(): FavoriteDao
}
