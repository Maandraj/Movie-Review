package com.example.moviereview.features.review.domain

import com.example.moviereview.features.review.data.NytimesRepo
import com.example.moviereview.features.review.domain.model.room.Favourites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NytimesInteractor @Inject constructor(
    private val nytimesRepo: NytimesRepo,
) {
    suspend fun getReviews(query: String = "") =
        nytimesRepo.getReviews(query)

    suspend fun getFavourites() =
        nytimesRepo.getFavourites()

    suspend fun getFavourites(query: String = "") =
        nytimesRepo.getFavourites(query)

    suspend fun saveFavourites(favourites: Favourites) =
        nytimesRepo.saveFavourite(favourites)

    suspend fun removeFavourites(favourites: Favourites) =
        nytimesRepo.removeFavourite(favourites)

}