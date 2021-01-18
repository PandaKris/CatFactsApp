package com.kristantosean.catfactsapp.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.local.getCatDatabase
import com.kristantosean.catfactsapp.network.CatFactNetwork
import com.kristantosean.catfactsapp.repository.CatRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception

class CatDetailViewModel(
    application: Application,
    private val catRepository: CatRepository,
    id: String
) : AndroidViewModel(application) {

    private var _cat = MutableLiveData<CatFact?>()
    val cat: LiveData<CatFact?> get() = _cat

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _eventNetworkError = MutableLiveData<Exception?>(null)
    val eventNetworkError: LiveData<Exception?> get() = _eventNetworkError

    init {
        refreshDataFromRepository(id)
    }

    fun refreshDataFromRepository(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = catRepository.getCatByID(id)
                _cat.value = result.data
                _eventNetworkError.value = result.error
            } catch (e: Exception) {
                _eventNetworkError.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(val app: Application, val id: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatDetailViewModel(
                    app,
                    CatRepository(getCatDatabase(app), CatFactNetwork.catFactAPI),
                    id
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}