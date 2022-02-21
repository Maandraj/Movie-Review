package com.example.moviereview.features.review.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewsRes(
    @Json(name = "results")
    val resultsRes: List<ResultRes>?,
    @Json(name = "status")
    val status: String,
    @Json(name = "copyright")
    val copyright: String,
    @Json(name = "has_more")
    val hasMore: Boolean,
    @Json(name = "num_results")
    val numResults: Int,
)
