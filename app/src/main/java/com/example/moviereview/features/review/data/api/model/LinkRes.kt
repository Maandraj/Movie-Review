package com.example.moviereview.features.review.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkRes(
    @Json(name = "suggested_link_text")
    val suggestedLinkTextRes: String,
    @Json(name = "type")
    val typeRes: String,
    @Json(name = "url")
    val urlRes: String?
)