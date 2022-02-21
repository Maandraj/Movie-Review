package com.example.moviereview.features.review.di

import com.example.moviereview.features.review.data.api.NytimesApi
import com.example.moviereview.features.review.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@InstallIn(SingletonComponent::class)
@Module
class ReviewFeatureModule {
    @Provides
    fun provideMovieApi(retrofit: Retrofit) = retrofit.create(NytimesApi::class.java)

}