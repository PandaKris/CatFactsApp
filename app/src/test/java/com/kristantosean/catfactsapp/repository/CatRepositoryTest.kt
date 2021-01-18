package com.kristantosean.catfactsapp.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.kristantosean.catfactsapp.MainApplication
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.ResultContainer
import com.kristantosean.catfactsapp.data.local.CatDao
import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.data.local.CatFactLocal
import com.kristantosean.catfactsapp.network.CatFactService
import org.assertj.core.api.Assertions.assertThat
import com.kristantosean.catfactsapp.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.*
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CatDetailViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var catDatabase: CatDatabase

    @Mock
    private lateinit var catDao: CatDao

    @Mock
    private lateinit var catFactService: CatFactService

    @Before
    fun setUp() {
    }

    @Test
    fun `return list when success`() {
        runBlocking {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val cat = listOf(CatFact("test_id", "test", date, false, "api"))
            doReturn(cat).`when`(catFactService).getCatFacts("cat", 10)
            doReturn(catDao).`when`(catDatabase).catDao

            // Act
            val repository = CatRepository(catDatabase, catFactService)
            val cats = repository.refreshCats()

            // Assert
            assert(cats == ResultContainer(cat, null))
        }
    }

    @Test
    fun `return error and local data when list fails`() {
        runBlocking {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val cat = listOf(CatFact("test_id", "test", date, false, "api"))
            val catLocal = listOf(CatFactLocal("test_id", "test", "2014-12-22T10:15:30.00Z", false, "api"))
            val exception = RuntimeException()
            doThrow(exception).`when`(catFactService).getCatFacts("cat", 10)
            doReturn(catLocal).`when`(catDao).getCatFacts()
            doReturn(catDao).`when`(catDatabase).catDao

            // Act
            val repository = CatRepository(catDatabase, catFactService)
            val cats = repository.refreshCats()

            // Assert
            assert(cats == ResultContainer(cat, exception))
        }
    }

    @Test
    fun `return data when success retrieve by id`() {
        runBlocking {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val cat = CatFact("test_id", "test", date, false, "api")
            doReturn(cat).`when`(catFactService).getCatFactDetail("1")
            doReturn(catDao).`when`(catDatabase).catDao

            // Act
            val repository = CatRepository(catDatabase, catFactService)
            val cats = repository.getCatByID("1")

            // Assert
            assert(cats == ResultContainer(cat, null))
        }
    }

    @Test
    fun `return error and local data when detail api fails`() {
        runBlocking {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val cat = CatFact("test_id", "test", date, false, "api")
            val catLocal = CatFactLocal("test_id", "test", "2014-12-22T10:15:30.00Z", false, "api")
            val exception = RuntimeException()
            doThrow(exception).`when`(catFactService).getCatFactDetail("1")
            doReturn(catLocal).`when`(catDao).getCatFactByID("1")
            doReturn(catDao).`when`(catDatabase).catDao

            // Act
            val repository = CatRepository(catDatabase, catFactService)
            val cats = repository.getCatByID("1")

            // Assert
            assert(cats == ResultContainer(cat, exception))
        }
    }

    @After
    fun tearDown() {

    }
}

