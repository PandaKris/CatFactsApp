package com.kristantosean.catfactsapp.network.di

import com.google.gson.GsonBuilder
import com.kristantosean.catfactsapp.network.CatFactService
import com.kristantosean.catfactsapp.utils.Constants
import com.kristantosean.catfactsapp.utils.ThreeTenGsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        val threeTenGsonAdapter = ThreeTenGsonAdapter.registerLocalDate(GsonBuilder()).create()
        return Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create(threeTenGsonAdapter))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesCatFactsService(retrofit: Retrofit): CatFactService {
        return retrofit.create(CatFactService::class.java)
    }
}