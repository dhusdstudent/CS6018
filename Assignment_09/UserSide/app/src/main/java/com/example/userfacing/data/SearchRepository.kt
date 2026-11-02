package com.example.userfacing.data

import android.app.appsearch.SearchResult

interface SearchRepository{
    suspend fun search (prompt: String): SearchResults
}

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