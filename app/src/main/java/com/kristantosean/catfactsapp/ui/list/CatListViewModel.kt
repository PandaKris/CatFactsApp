package com.kristantosean.catfactsapp.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.repository.CatRepository
import kotlinx.coroutines.launch

class CatListViewModel @ViewModelInject constructor(private val catRepository: CatRepository) : ViewModel() {

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
}