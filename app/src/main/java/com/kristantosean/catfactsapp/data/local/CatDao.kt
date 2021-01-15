package com.kristantosean.catfactsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatDao {

    @Query("select * from catfactlocal")
    fun getCatFacts(): List<CatFactLocal>

    @Query("select * from catfactlocal WHERE id = :id")
    fun getCatFactByID(id: String): CatFactLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cats: List<CatFactLocal>)

    @Query("DELETE FROM catfactlocal")
    fun deleteAll(): Unit

    @Update
    fun update(cat: CatFactLocal)

}