package com.paymob.moviesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val movieId: Int? = null,
)