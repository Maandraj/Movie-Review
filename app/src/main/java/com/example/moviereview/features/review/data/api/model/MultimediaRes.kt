package com.example.moviereview.features.review.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MultimediaRes(
    @Json(name = "height")
    val heightRes: Int,
    @Json(name = "src")
    val srcRes: String,
    @Json(name = "type")
    val typeRes: String,
    @Json(name = "width")
    val widthRes: Int
)