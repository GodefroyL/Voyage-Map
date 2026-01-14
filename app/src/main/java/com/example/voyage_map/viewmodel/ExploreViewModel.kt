package com.example.voyage_map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyage_map.data.api.WikiPage
import com.example.voyage_map.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    private val repository = TravelRepository()

    private val _results = MutableStateFlow<List<WikiPage>>(emptyList())
    val results: StateFlow<List<WikiPage>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.searchPlaces(query)
                _results.value = response.query?.search ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _results.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

}
