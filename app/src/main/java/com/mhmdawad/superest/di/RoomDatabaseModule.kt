package com.mhmdawad.superest.di

import android.content.Context
import androidx.room.Room
import com.mhmdawad.superest.data.database.FavoriteDatabase
import com.mhmdawad.superest.util.FAVORITE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            FAVORITE_DATABASE
        ).build()


    @Singleton
    @Provides
    fun provideYourDao(db: FavoriteDatabase) = db.getFavoriteDao()
}