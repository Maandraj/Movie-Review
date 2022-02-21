package com.example.moviereview.features.review.domain.model


data class Result(
    val byline: String,
    val dateUpdated: String,
    val displayTitle: String,
    val headline: String,
    val url: String?,
    val src: String?,
    val publicationDate: String,
    val description: String,
    var favourite : Boolean = false,
)