package com.vp.db

import com.vp.db.dao.FavDao
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideFavDao(database: AppDatabase): FavDao {
        return database.favDao()
    }
}