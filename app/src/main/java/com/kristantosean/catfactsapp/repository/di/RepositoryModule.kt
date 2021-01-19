package com.kristantosean.catfactsapp.repository.di

import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.network.CatFactService
import com.kristantosean.catfactsapp.repository.CatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesCatRepository(catDatabase: CatDatabase, catFactService: CatFactService): CatRepository {
        return CatRepository(catDatabase, catFactService)
    }
}