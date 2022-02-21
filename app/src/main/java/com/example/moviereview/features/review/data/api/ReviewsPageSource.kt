package com.example.moviereview.features.review.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviereview.features.review.data.api.model.mapper.ResultsMapper
import com.example.moviereview.features.review.data.local.FavoritesDao
import com.example.moviereview.features.review.domain.model.Result
import com.example.moviereview.features.review.domain.model.room.Favourites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class ReviewsPageSource (
    private val api: NytimesApi,
    private val query: String,
    private val resultsMapper: ResultsMapper,
    private val favoritesDao: FavoritesDao,

) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(ELEMENTS) ?: page.nextKey?.minus(ELEMENTS)

    }
    /**
     * Подготовка данных для показа, если есть в избранном(room) меняем статус favourite на true
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
    try {
        val page: Int = params.key ?: 0
        val pageSize: Int = params.loadSize.coerceAtMost(MAX_RESULT)
        val response = api.getReviews(query, page)
        return if (response.isSuccessful) {
            val result =  checkNotNull(response.body()?.resultsRes).map {  resultsMapper.map(it) }
            var favourites: List<Favourites?>?

            withContext(Dispatchers.IO){
                favourites = favoritesDao.getAll()
           }
            val resultsSort = result.toMutableList()
            resultsSort.forEachIndexed { index, res ->
                if (favourites != null) {
                    for (fav in favourites!!)
                        if (res.url == fav?.url) {
                            resultsSort[index] = res.copy(favourite = true)
                            break
                        }
                }

            }
            val nextKey = if (resultsSort.size < pageSize) null else page + ELEMENTS
            LoadResult.Page(resultsSort, prevKey= null, nextKey = nextKey)
        } else {
            LoadResult.Error(HttpException(response))
        }


    }catch (ex : Exception){
        return  LoadResult.Error(ex)

    }
    }
}