package com.vp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.db.ListItemEntity

@Dao
interface FavDao {

    @Query("SELECT * FROM fav")
    fun getAllMovies(): LiveData<List<ListItemEntity>>

    @Query("SELECT EXISTS(SELECT * FROM fav WHERE imdbID = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: ListItemEntity): Long

    @Query("DELETE FROM fav WHERE imdbID = :id")
    suspend fun removeFavorite(id: String)
}