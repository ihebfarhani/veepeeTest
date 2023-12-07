package com.vp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav")
data class ListItemEntity(
    @PrimaryKey
    val imdbID: String,
    val poster: String,
)
