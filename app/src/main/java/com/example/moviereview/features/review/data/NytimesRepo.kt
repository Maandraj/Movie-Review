package com.example.moviereview.features.review.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.moviereview.features.review.data.api.NytimesApi
import com.example.moviereview.features.review.data.api.ReviewsPageSource
import com.example.moviereview.features.review.data.api.model.mapper.ResultsMapper
import com.example.moviereview.features.review.data.local.FavoritesDao
import com.example.moviereview.features.review.domain.model.room.Favourites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NytimesRepo @Inject constructor(
    private val resultMapper:ResultsMapper,
    private val favoritesDao: FavoritesDao,
    private val api: NytimesApi,
) {
    suspend fun getReviews(query: String = "") = withContext(Dispatchers.IO) {
        val pager = Pager(config = PagingConfig(pageSize = 3),
            pagingSourceFactory = {
          ReviewsPageSource(api, query, resultMapper, favoritesDao)
        })
        pager.liveData
    }

    suspend fun getFavourites() = withContext(Dispatchers.IO) {
        favoritesDao.getAll()
    }
    suspend fun getFavourites(query: String) = withContext(Dispatchers.IO) {
        favoritesDao.getByQuery(query)
    }
    suspend fun saveFavourite(favourites: Favourites) = withContext(Dispatchers.IO) {
        favoritesDao.insert(favourites)

    }
    suspend fun removeFavourite(favourites: Favourites) = withContext(Dispatchers.IO) {
        favoritesDao.delete(favourites)

    }
}