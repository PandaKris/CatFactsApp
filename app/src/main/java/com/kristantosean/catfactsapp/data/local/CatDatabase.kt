package com.kristantosean.catfactsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CatFactLocal::class], version = 7)
abstract class CatDatabase: RoomDatabase() {
    abstract val catDao: CatDao
}

private lateinit var INSTANCE: CatDatabase

fun getCatDatabase(context: Context): CatDatabase {
    synchronized(CatDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, CatDatabase::class.java, "cats")
                .fallbackToDestructiveMigration().build()
        }
    }

    return INSTANCE
}