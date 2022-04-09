package com.mhmdawad.superest.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mhmdawad.superest.BuildConfig
import com.mhmdawad.superest.data.networking.ApiService
import com.mhmdawad.superest.util.BACKEND_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(httpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}