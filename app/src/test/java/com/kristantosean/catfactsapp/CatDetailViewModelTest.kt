package com.kristantosean.catfactsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.ResultContainer
import org.assertj.core.api.Assertions.assertThat
import com.kristantosean.catfactsapp.repository.CatRepository
import com.kristantosean.catfactsapp.ui.detail.CatDetailViewModel
import com.kristantosean.catfactsapp.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.argumentCaptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CatDetailViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var catRepository: CatRepository

    @Mock
    private lateinit var catFactObserver: Observer<CatFact?>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    @Mock
    private lateinit var networkErrorObserver: Observer<Exception?>

    @Before
    fun setUp() {
    }

    @Test
    fun `return data when success`() {
        testCoroutineRule.runBlockingTest {

            // Arrange
            val date = LocalDateTime.now(
                Clock.fixed(
                    Instant.parse("2014-12-22T10:15:30.00Z"),
                    ZoneId.of("UTC")
                )
            )
            val cat = CatFact("test_id", "test", date, false, "api")
            doReturn(ResultContainer(cat, null)).`when`(catRepository)
                .getCatByID(ArgumentMatchers.anyString())

            // Act
            val viewModel = CatDetailViewModel(catRepository)
            viewModel.isLoading.observeForever(loadingObserver)
            viewModel.cat.observeForever(catFactObserver)
            viewModel.eventNetworkError.observeForever(networkErrorObserver)
            viewModel.refreshDataFromRepository("1")

            // Assert
            argumentCaptor<CatFact>().apply {
                verify(catFactObserver, times(1)).onChanged(capture())
                assertThat(allValues).containsSequence(cat)
            }

            argumentCaptor<Boolean>().apply {
                verify(loadingObserver, times(3)).onChanged(capture())
            }

            argumentCaptor<Exception>().apply {
                verify(networkErrorObserver, times(2)).onChanged(capture())
            }
        }
    }

    @Test
    fun `return error with data when network error`() {
        testCoroutineRule.runBlockingTest {

            // Arrange
            val date = LocalDateTime.now(
                Clock.fixed(
                    Instant.parse("2014-12-22T10:15:30.00Z"),
                    ZoneId.of("UTC")
                )
            )
            val cat = CatFact("test_id", "test", date, false, "api")
            val exception = Exception("exception")
            doReturn(ResultContainer(cat, exception)).`when`(catRepository).getCatByID(
                ArgumentMatchers.anyString()
            )

            // Act
            val viewModel = CatDetailViewModel(catRepository)
            viewModel.isLoading.observeForever(loadingObserver)
            viewModel.cat.observeForever(catFactObserver)
            viewModel.eventNetworkError.observeForever(networkErrorObserver)
            viewModel.refreshDataFromRepository("1")

            // Assert
            argumentCaptor<CatFact>().apply {
                verify(catFactObserver, times(1)).onChanged(capture())
                assertThat(allValues).containsSequence(cat)
            }

            argumentCaptor<Boolean>().apply {
                verify(loadingObserver, times(3)).onChanged(capture())
            }

            argumentCaptor<Exception>().apply {
                verify(networkErrorObserver, times(2)).onChanged(capture())
                assertThat(allValues).contains(exception)
            }
        }
    }

    @After
    fun tearDown() {

    }
}

