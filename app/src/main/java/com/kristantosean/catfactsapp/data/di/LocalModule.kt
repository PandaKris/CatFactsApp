package com.kristantosean.catfactsapp.data.di

import android.app.Application
import android.content.Context
import com.kristantosean.catfactsapp.MainApplication
import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.data.local.getCatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun providesCatDatabase(@ApplicationContext appContext: Context): CatDatabase {
        return getCatDatabase(appContext)
    }


}