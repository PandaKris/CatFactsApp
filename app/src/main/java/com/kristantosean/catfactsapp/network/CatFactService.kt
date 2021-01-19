package com.kristantosean.catfactsapp.network

import com.kristantosean.catfactsapp.data.CatFact
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