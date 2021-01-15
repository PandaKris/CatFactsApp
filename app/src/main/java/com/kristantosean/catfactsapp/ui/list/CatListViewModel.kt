package com.kristantosean.catfactsapp.ui.list

import android.app.Application
import androidx.lifecycle.*
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.data.local.getCatDatabase
import com.kristantosean.catfactsapp.repository.CatRepository
import kotlinx.coroutines.launch
import java.io.IOException

class CatListViewModel(application: Application, private val catRepository: CatRepository) : AndroidViewModel(application) {

    private var _cats = MutableLiveData<List<CatFact>>(listOf())
    val cats: LiveData<List<CatFact>> get() = _cats

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _eventNetworkError = MutableLiveData<Exception?>(null)
    val eventNetworkError: LiveData<Exception?> get() = _eventNetworkError

    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = catRepository.refreshCats()
                _cats.value = result.data
                _eventNetworkError.value = result.error
           } catch (e: Exception) {
                e.printStackTrace()
                _eventNetworkError.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatListViewModel(app, CatRepository(getCatDatabase(app))) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}