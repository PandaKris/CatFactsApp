package com.kristantosean.catfactsapp.repository

import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.ResultContainer
import com.kristantosean.catfactsapp.data.asDatabaseModel
import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.data.local.asDomainModel
import com.kristantosean.catfactsapp.network.CatFactService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CatRepository @Inject constructor(private val database: CatDatabase, private val catService: CatFactService) {

    suspend fun refreshCats() : ResultContainer<List<CatFact>> {
        return withContext(Dispatchers.IO) {
            try {
                val catFacts = catService.getCatFacts("cat", 10)
                database.catDao.deleteAll()
                database.catDao.insertAll(catFacts.asDatabaseModel())
                ResultContainer(catFacts, null)
            } catch (e: Exception) {
                ResultContainer(database.catDao.getCatFacts().map { it.asDomainModel() }, e)
            }
        }
    }

    suspend fun getCatByID(id: String) : ResultContainer<CatFact> {
        return withContext(Dispatchers.IO) {
            try {
                val catFact = catService.getCatFactDetail(id)
                database.catDao.update(catFact.asDatabaseModel())
                ResultContainer(catFact, null)
            } catch (e: Exception) {
                ResultContainer(database.catDao.getCatFactByID(id)?.asDomainModel(), e)
            }
        }
    }
}