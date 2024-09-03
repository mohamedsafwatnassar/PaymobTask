package com.paymob.moviesapp.localDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paymob.moviesapp.model.FavoriteModel
import com.paymob.moviesapp.model.MovieItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteModel: FavoriteModel)

    @Query("select * From favorite_table")
    fun getAllFavorites():  List<FavoriteModel>

    @Query("DELETE FROM favorite_table WHERE movieId = :movieId")
    fun deleteFavorite(movieId: Int)
}