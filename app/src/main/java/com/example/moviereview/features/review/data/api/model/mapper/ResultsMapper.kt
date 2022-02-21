package com.example.moviereview.features.review.data.api.model.mapper

import com.example.moviereview.HOME_URL
import com.example.moviereview.features.review.data.api.model.ResultRes
import com.example.moviereview.features.review.domain.model.Result
import javax.inject.Inject

class ResultsMapper @Inject constructor(
){
    fun map(res: ResultRes) = Result(
        byline = res.bylineRes ?: "",
        dateUpdated = res.dateUpdatedRes ?: "",
        displayTitle = res.displayTitleRes ?: "",
        headline = res.headlineRes ?: "",
        publicationDate = res.publicationDateRes ?: "",
        description = res.summaryShortRes ?: "",
        src =res.multimediaRes?.srcRes,
        url = res.linkRes?.urlRes ?: HOME_URL
    )
}