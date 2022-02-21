package com.example.moviereview.features.review.presentation.interfaces

import com.example.moviereview.features.review.domain.model.room.Favourites


interface ClickRemoveListener{
    fun removeFavouriteClick(favourites: Favourites, position: Int)
}
interface ClickAddListener{
    fun addFavouriteClick(favourites:Favourites, position: Int)
}