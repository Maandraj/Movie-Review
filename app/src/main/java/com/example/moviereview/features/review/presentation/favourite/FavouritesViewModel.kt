package com.example.moviereview.features.review.presentation.favourite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereview.features.review.domain.NytimesInteractor
import com.example.moviereview.features.review.domain.model.room.Favourites
import com.example.moviereview.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val interactor: NytimesInteractor,
) : ViewModel() {


    private val _favourites = MutableLiveData<List<Favourites>>()
    val favourites = _favourites.asLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()

    private val _searching = MutableLiveData(false)
    val searching = _searching.asLiveData()

    private val _exception = MutableLiveData<Exception>()
    val exception = _exception.asLiveData()

    fun getFavourites(query : String = "" ) = viewModelScope.launch {
        _loading.postValue(true)
        try {
            if (query.trim() == "")
                _favourites.postValue(interactor.getFavourites()?.filterNotNull())
            else{
                _favourites.postValue(interactor.getFavourites(query)?.filterNotNull())

            }
        } catch (ex: Exception) {
            _exception.postValue(ex)
        }
        _loading.postValue(false)
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