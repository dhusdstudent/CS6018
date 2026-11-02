package com.example.userfacing

import android.app.appsearch.SearchResult
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userfacing.data.SearchRepository
import com.example.userfacing.data.SearchResults
import com.example.userfacing.data.SearchUIState
import com.example.userfacing.data.TestSearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = TestSearchRepository()

    private val _uiState = MutableStateFlow(SearchUIState())

    val uiState: StateFlow<SearchUIState> =
        _uiState.asStateFlow()

    fun updatePrompt(value: String) {
        _uiState.update {
            it.copy(prompt = value)
        }
    }

    fun selectModel(model: Int) {
        if (model !in 0..1) return

        _uiState.update {
            it.copy(selectedModel = model)
        }
    }

    fun runSearch() {
        val currentPrompt = _uiState.value.prompt

        if (currentPrompt.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try{
                val result= repository.search(currentPrompt)

                _uiState.update {
                    it.copy(
                        searchResult = result,
                        selectedModel = 0
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Error"
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

}