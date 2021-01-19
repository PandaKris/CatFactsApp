package com.kristantosean.catfactsapp.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.repository.CatRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class CatDetailViewModel @ViewModelInject constructor(private val catRepository: CatRepository) : ViewModel() {

    private var _cat = MutableLiveData<CatFact?>()
    val cat: LiveData<CatFact?> get() = _cat

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _eventNetworkError = MutableLiveData<Exception?>(null)
    val eventNetworkError: LiveData<Exception?> get() = _eventNetworkError

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

}