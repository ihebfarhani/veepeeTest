package com.vp.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.db.ListItemEntity
import com.vp.db.dao.FavDao
import com.vp.detail.DetailActivity
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val favDao: FavDao
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    fun isFavorite(): LiveData<Boolean> = isFavorite

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails() {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(DetailActivity.queryProvider.getMovieId())
            .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                override fun onResponse(
                    call: Call<MovieDetail>?,
                    response: Response<MovieDetail>?
                ) {
                    details.postValue(response?.body())

                    response?.body()?.title?.let {
                        title.postValue(it)
                    }

                    loadingState.value = LoadingState.LOADED
                }

                override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                    details.postValue(null)
                    loadingState.value = LoadingState.ERROR
                }
            })
    }


    fun doFav() {
        Log.e("isFavorite", isFavorite.value.toString())
        if (!isFavorite.value!!) {
            addFavorite()
        } else deleteFavorite()
    }

    private fun addFavorite() {
        viewModelScope.launch {
            favDao.addFavorite(
                ListItemEntity(
                    DetailActivity.queryProvider.getMovieId(), details.value?.poster ?: ""
                )
            ) != -1L
            isFavorite.value = true
        }
    }

    fun checkIfFav() = viewModelScope.launch {
        isFavorite.postValue(favDao.isFavorite(DetailActivity.queryProvider.getMovieId()))
    }

    private fun deleteFavorite() {
        viewModelScope.launch {
            favDao.removeFavorite(DetailActivity.queryProvider.getMovieId())
            isFavorite.value = false
        }
    }


    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}