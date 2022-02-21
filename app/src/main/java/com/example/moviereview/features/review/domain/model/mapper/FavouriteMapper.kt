package com.example.moviereview.features.review.domain.model.mapper

import com.example.moviereview.HOME_URL
import com.example.moviereview.features.review.domain.model.Result
import com.example.moviereview.features.review.domain.model.room.Favourites
import javax.inject.Inject

class FavouriteMapper @Inject constructor(
){
    fun map(res: Result) = Favourites(
        byline = res.byline,
        dateUpdated = res.dateUpdated,
        displayTitle = res.displayTitle ,
        headline = res.headline ,
        publicationDate = res.publicationDate,
        description = res.description,
        src =res.src,
        url = res.url ?: HOME_URL
    )
}