package com.example.moviereview.features.review.data.local

import androidx.room.*

import com.example.moviereview.features.review.domain.model.room.Favourites


@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favourites")
    fun getAll(): List<Favourites?>?

    @Query("SELECT * FROM favourites WHERE displayTitle LIKE :query OR headline LIKE :query OR byline LIKE :query OR description LIKE :query ")
    fun getByQuery(query: String): List<Favourites?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: Favourites?)

    @Delete()
    fun delete(favourite: Favourites?)
}