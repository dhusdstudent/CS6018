package com.example.userfacing

interface SearchRepository{
    suspend fun search (prompt: String): SearchResults
}

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

class TestSearchRepository {

    suspend fun search(prompt: String): SearchResults {

        val responseA =
            "Regular AI response to: $prompt\n\n" +
                    "Bad response I'm sure"

        val responseB =
            "RAGS response to: $prompt\n\n" +
                    "Good response! Yay!"

        return SearchResults(
            modelA = responseA,
            modelB = responseB
        )
    }
}