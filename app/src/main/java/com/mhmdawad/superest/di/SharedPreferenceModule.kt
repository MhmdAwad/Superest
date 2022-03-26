package com.mhmdawad.superest.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class SharedPreferenceModule {

    @ViewModelScoped
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
}