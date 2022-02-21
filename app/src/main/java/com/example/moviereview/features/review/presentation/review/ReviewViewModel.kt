package com.example.moviereview.features.review.presentation.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviereview.features.review.domain.NytimesInteractor
import com.example.moviereview.features.review.domain.model.Result
import com.example.moviereview.features.review.domain.model.room.Favourites
import com.example.moviereview.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val interactor: NytimesInteractor,
) : ViewModel() {



    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()

    private val _exception = MutableLiveData<Exception>()
    val exception = _exception.asLiveData()

    private val _searching = MutableLiveData(false)
    val searching = _searching.asLiveData()

    suspend fun getReviews(
        query: String = "",
    ): LiveData<PagingData<Result>> {
        return interactor.getReviews(query).cachedIn(viewModelScope)
    }

    fun saveFavourites(favourites: Favourites) = viewModelScope.launch {
        try {
            Log.i("Room", "Added")
            interactor.saveFavourites(favourites)
        } catch (ex: Exception) {
            _exception.postValue(ex)
        }
    }

    fun removeFavourites(favourites: Favourites) = viewModelScope.launch {
        try {
            Log.i("Room", "Remove")

            interactor.removeFavourites(favourites)
        } catch (ex: Exception) {
            _exception.postValue(ex)
        }
    }
    fun setStateSearch(show:Boolean = false){
        viewModelScope.launch {
            _searching.postValue(show)
        }
    }
}