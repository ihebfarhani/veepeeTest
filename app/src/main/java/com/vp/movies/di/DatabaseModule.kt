package com.vp.movies.di

import android.app.Application
import androidx.room.Room
import com.vp.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, "movie_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}