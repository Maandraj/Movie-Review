package com.example.moviereview.features.review.data.api

import com.example.moviereview.BuildConfig
import com.example.moviereview.features.review.data.api.model.ReviewsRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NytimesApi {

    @GET("reviews/search.json")
    suspend fun getReviews(
                           @Query("query") query : String = "",
                           @Query("offset") offset : Int = 0,
                           @Query("api-key") apiKey : String = BuildConfig.API_KEY) : Response<ReviewsRes>
}
const val MAX_RESULT = 20
const val ELEMENTS = 20