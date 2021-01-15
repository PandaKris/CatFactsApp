package com.kristantosean.catfactsapp.ui.list

import android.app.Application
import androidx.lifecycle.*
import com.kristantosean.catfactsapp.data.local.getCatDatabase
import com.kristantosean.catfactsapp.repository.CatRepository
import kotlinx.coroutines.launch
import java.io.IOException

class CatListViewModel(application: Application) : AndroidViewModel(application) {

    private val catFactRepository = CatRepository(getCatDatabase(application))
    val cats = catFactRepository.catFacts

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                catFactRepository.refreshCats()
                _eventNetworkError.value = false
           } catch (networkError: Exception) {
                networkError.printStackTrace()
                _eventNetworkError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}