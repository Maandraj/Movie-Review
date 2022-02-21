package com.example.moviereview.features.review.domain

import com.example.moviereview.features.review.data.NytimesRepo
import com.example.moviereview.features.review.domain.model.room.Favourites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NytimesInteractor @Inject constructor(
    private val nytimesRepo: NytimesRepo
) {
    suspend fun getReviews(query:String = "") = withContext(Dispatchers.IO) {
        nytimesRepo.getReviews(query)
    }
    suspend fun getFavourites() = withContext(Dispatchers.IO) {
        nytimesRepo.getFavourites()
    }
    suspend fun getFavourites(query:String = "") = withContext(Dispatchers.IO) {
        nytimesRepo.getFavourites(query)
    }
    suspend fun saveFavourites(favourites: Favourites) = withContext(Dispatchers.IO) {
        nytimesRepo.saveFavourite(favourites)
    }
    suspend fun removeFavourites(favourites: Favourites) = withContext(Dispatchers.IO) {
        nytimesRepo.removeFavourite(favourites)
    }
}