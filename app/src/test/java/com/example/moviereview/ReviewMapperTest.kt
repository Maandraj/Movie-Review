package com.example.moviereview

import com.example.moviereview.features.review.data.api.model.LinkRes
import com.example.moviereview.features.review.data.api.model.MultimediaRes
import com.example.moviereview.features.review.data.api.model.ResultRes
import com.example.moviereview.features.review.data.api.model.mapper.ResultsMapper
import com.example.moviereview.features.review.domain.model.mapper.FavouriteMapper
import org.junit.Assert
import org.junit.Test

class ReviewMapperTest {
    private val resultsMapper = ResultsMapper()
    private val favouriteMapper = FavouriteMapper()

    @Test
    fun mapToResultDomainData() {
        val inputData = getTestDataResult()

        var outputData = resultsMapper.map(inputData)

        outputData.apply {
            Assert.assertEquals(byline, "Test")
            Assert.assertEquals(dateUpdated, "2021.01.10")
            Assert.assertEquals(displayTitle, "TestTitle")
            Assert.assertEquals(src, HOME_URL)
            Assert.assertEquals(publicationDate, "2021.02.10")
            Assert.assertEquals(description, "DescTest")
            Assert.assertEquals(headline, "TestLine")
            Assert.assertEquals(url, HOME_URL)
        }
        outputData = outputData.copy(favourite = true)
        val favourites = resultsMapper.mapToFavourite(mutableListOf(outputData),
            mutableListOf(favouriteMapper.map(outputData)))

        favourites[0].apply {
            Assert.assertEquals(favourite, false)
        }
    }

    private fun getTestDataResult() = ResultRes(
        bylineRes = "Test",
        dateUpdatedRes = "2021.01.10",
        displayTitleRes = "TestTitle",
        headlineRes = "TestLine",
        publicationDateRes = "2021.02.10",
        summaryShortRes = "DescTest",
        multimediaRes = MultimediaRes(512, HOME_URL, "mediumThreeByTwo210", 512),
        linkRes = LinkRes("Link", "article", HOME_URL),
        criticsPickRes = 5,
        mpaaRatingRes = "test",
        openingDateRes = "2020.02.10",
    )
}