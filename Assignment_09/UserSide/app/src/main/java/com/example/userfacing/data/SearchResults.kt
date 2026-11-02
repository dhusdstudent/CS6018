package com.example.userfacing.data

import android.app.appsearch.SearchResult

data class SearchResults (
    val modelA: String,
    val modelB: String
)

data class SearchUIState(
    val prompt: String = "",
    val searchResult: SearchResults? = null,
    val selectedModel: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)