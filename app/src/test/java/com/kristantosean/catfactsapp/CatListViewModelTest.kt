package com.kristantosean.catfactsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.ResultContainer
import org.assertj.core.api.Assertions.assertThat
import com.kristantosean.catfactsapp.repository.CatRepository
import com.kristantosean.catfactsapp.ui.list.CatListViewModel
import com.kristantosean.catfactsapp.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

            // Assert
            argumentCaptor<List<CatFact>>().apply {
                verify(catFactsObserver, times(1)).onChanged(capture())
                assertThat(allValues).containsSequence(list)
            }

            argumentCaptor<Boolean>().apply {
                verify(loadingObserver, times(1)).onChanged(capture())
            }

            argumentCaptor<Exception>().apply {
                verify(networkErrorObserver, times(1)).onChanged(capture())
            }
        }
    }

    @Test
    fun `return error with data when network error`() {
        testCoroutineRule.runBlockingTest {

            // Arrange
            val date = LocalDateTime.now(Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC")))
            val list = listOf(
                CatFact("test_id", "test", date, false, "api")
            )
            val exception = Exception("exception")
            Mockito.doReturn(ResultContainer(list, exception)).`when`(catRepository).refreshCats()

            // Act
            val viewModel = CatListViewModel(mainApplication, catRepository)
            viewModel.isLoading.observeForever(loadingObserver)
            viewModel.cats.observeForever(catFactsObserver)
            viewModel.eventNetworkError.observeForever(networkErrorObserver)

            // Assert
            argumentCaptor<List<CatFact>>().apply {
                verify(catFactsObserver, times(1)).onChanged(capture())
                assertThat(allValues).containsSequence(list)
            }

            argumentCaptor<Boolean>().apply {
                verify(loadingObserver, times(1)).onChanged(capture())
            }

            argumentCaptor<Exception>().apply {
                verify(networkErrorObserver, times(1)).onChanged(capture())
                assertThat(allValues).contains(exception)
            }
        }
    }

    @After
    fun tearDown() {

    }
}

