package com.example.userfacing.myui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userfacing.SearchViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel()
) {
    val uiState by searchViewModel.uiState.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = "Regular AI vs. RAGS Search Comparison",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = uiState.prompt,
            onValueChange = {
                searchViewModel.updatePrompt(it)
            },
            label = { Text("Enter your prompt") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 6,
            enabled = !uiState.isLoading
        )

        Button(
            onClick = {
                searchViewModel.runSearch()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.prompt.isNotBlank() && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Search")
            }
        }

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        Text(
            text = "Model Responses",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { searchViewModel.selectModel(0) },
                enabled = uiState.searchResult != null,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (uiState.selectedModel == 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                )
            ) {
                Text("Regular AI")
            }

            Button(
                onClick = { searchViewModel.selectModel(1) },
                modifier = Modifier.weight(1f),
                enabled = uiState.searchResult != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (uiState.selectedModel == 1) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                )
            ) {
                Text("RAGS AI")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = if (uiState.selectedModel == 0) {
                        "Regular AI's Response"
                    } else {
                        "RAGS response"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                val response = when (uiState.selectedModel) {
                    0 -> uiState.searchResult?.modelA
                    else -> uiState.searchResult?.modelB
                }

                Text(
                    text = response
                        ?: "The AI response will appear here"
                )
            }
        }
    }
}
