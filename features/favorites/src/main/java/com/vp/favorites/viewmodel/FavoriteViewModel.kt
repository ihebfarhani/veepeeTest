package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vp.db.ListItemEntity
import com.vp.db.dao.FavDao
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favDao: FavDao) : ViewModel() {
    val allMovies: LiveData<List<ListItemEntity>> = favDao.getAllMovies()
}