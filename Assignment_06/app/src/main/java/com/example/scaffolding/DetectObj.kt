package com.example.scaffolding

import androidx.compose.ui.geometry.Rect


data class DetectObj (
    val theBox: Rect,
    val label: String,
    val accuracy: Float
)



