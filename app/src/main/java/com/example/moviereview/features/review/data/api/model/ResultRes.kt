package com.example.moviereview.features.review.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultRes(
    @Json(name = "byline")
    val bylineRes: String?,
    @Json(name = "critics_pick")
    val criticsPickRes: Int?,
    @Json(name = "date_updated")
    val dateUpdatedRes: String?,
    @Json(name = "display_title")
    val displayTitleRes: String?,
    @Json(name = "headline")
    val headlineRes: String?,
    @Json(name = "link")
    val linkRes: LinkRes?,
    @Json(name = "mpaa_rating")
    val mpaaRatingRes: String?,
    @Json(name = "multimedia")
    val multimediaRes: MultimediaRes?,
    @Json(name = "opening_date")
    val openingDateRes: String?,
    @Json(name = "publication_date")
    val publicationDateRes: String?,
    @Json(name = "summary_short")
    val summaryShortRes: String?
)