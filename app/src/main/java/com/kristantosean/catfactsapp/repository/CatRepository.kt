package com.kristantosean.catfactsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.asDatabaseModel
import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.data.local.asDomainModel
import com.kristantosean.catfactsapp.network.CatFactNetwork
import com.kristantosean.catfactsapp.network.CatFactService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class CatRepository(private val database: CatDatabase) {

    var catID: String = ""

    suspend fun refreshCats() {
        withContext(Dispatchers.IO) {
            val catFacts = CatFactNetwork.catFactAPI.getCatFacts("cat", 10)
            database.catDao.deleteAll()
            database.catDao.insertAll(catFacts.asDatabaseModel())
        }
    }

    suspend fun getCatByID(id: String) : CatFact? {
        this.catID = id
        return withContext(Dispatchers.IO) {
            try {
                val catFact = CatFactNetwork.catFactAPI.getCatFactDetail(id)
                database.catDao.update(catFact.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            database.catDao.getCatFactByID(id)?.asDomainModel()
        }
    }

    val catFacts: LiveData<List<CatFact>> = Transformations.map(database.catDao.getCatFacts()) {
        it.asDomainModel()
    }

}