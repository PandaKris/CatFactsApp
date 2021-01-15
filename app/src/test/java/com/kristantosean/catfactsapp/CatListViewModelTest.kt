package com.kristantosean.catfactsapp

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.ResultContainer
import com.kristantosean.catfactsapp.data.local.CatDao
import com.kristantosean.catfactsapp.data.local.CatDatabase
import com.kristantosean.catfactsapp.data.local.CatFactLocal
import com.kristantosean.catfactsapp.data.local.getCatDatabase
import com.kristantosean.catfactsapp.network.CatFactNetwork
import com.kristantosean.catfactsapp.network.CatFactService
import com.kristantosean.catfactsapp.repository.CatRepository
import com.kristantosean.catfactsapp.ui.list.CatListViewModel
import com.kristantosean.catfactsapp.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CatListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var catRepository: CatRepository

    @Mock
    private lateinit var catFactsObserver: Observer<List<CatFact>>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    @Mock
    private lateinit var networkErrorObserver: Observer<Exception?>

    @Mock
    private lateinit var mainApplication: MainApplication

    @Before
    fun setUp() {
        val context  =  Mockito.mock(Context::class.java)
        Mockito.`when`(mainApplication.applicationContext).thenReturn(context)
    }

    @Test
    fun `return list when success`() {
        testCoroutineRule.runBlockingTest {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val list = listOf(
                CatFact("test_id", "test", date, false, "api")
            )
            Mockito.doReturn(ResultContainer(list, null)).`when`(catRepository).refreshCats()

            // Act
            val viewModel = CatListViewModel(mainApplication, catRepository)
            viewModel.isLoading.observeForever(loadingObserver)
            viewModel.cats.observeForever(catFactsObserver)
            viewModel.eventNetworkError.observeForever(networkErrorObserver)
            viewModel.refreshDataFromRepository()

            // Assert
            argumentCaptor<List<CatFact>>().apply {
                verify(catFactsObserver, times(2)).onChanged(capture())
            }

            argumentCaptor<Boolean>().apply {
                verify(loadingObserver, times(3)).onChanged(capture())
            }

            argumentCaptor<Exception>().apply {
                verify(networkErrorObserver, times(2)).onChanged(capture())
            }
        }


    }


    @After
    fun tearDown() {

    }

    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}

