package com.kristantosean.catfactsapp.network

import com.google.gson.GsonBuilder
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.utils.Constants
import com.kristantosean.catfactsapp.utils.ThreeTenGsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatFactService {

    @GET("/facts/random")
    suspend fun getCatFacts(
        @Query("animal_type") animalType: String,
        @Query("amount") amount: Int
    ) : List<CatFact>

    @GET("/facts/{id}")
    suspend fun getCatFactDetail(
        @Path(value="id", encoded = true) id: String
    ): CatFact
}

object CatFactNetwork {
    private val threeTenGsonAdapter = ThreeTenGsonAdapter.registerLocalDate(GsonBuilder()).create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create(threeTenGsonAdapter))
        .build()

    val catFactAPI: CatFactService = retrofit.create(CatFactService::class.java)
}