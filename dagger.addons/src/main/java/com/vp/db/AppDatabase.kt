package com.vp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.db.dao.FavDao


@Database(entities = [ListItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favDao(): FavDao
}