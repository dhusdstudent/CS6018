package com.example.assignment_03

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MaracaScreen(
    shakes: List<Shake>,
    onDeleteOldShakes: (Long) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        SnowGlobe(
            shakes = shakes,
            modifier = Modifier.weight(1f).fillMaxWidth()
        )

        Text(
            text = "Shakes detected: ${shakes.size}"
        )

        Button(
            onClick = {
                val deadline = 60 * 60 * 1000L
                val cutoff = System.currentTimeMillis() - deadline
                onDeleteOldShakes(cutoff)
            }
        ) {
            Text("Delete shake history that is older than one hour.")
        }
    }

}