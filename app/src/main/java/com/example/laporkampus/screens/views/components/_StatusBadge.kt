package com.example.laporkampus.screens.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    // Determine background and text color based on the report status
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "DONE" -> Color(0xFFD4EDDA) to Color(0xFF155724)        // Soft Green
        "PROCESSED" -> Color(0xFFCCE5FF) to Color(0xFF004085)   // Soft Blue
        "PENDING" -> Color(0xFFFFF3CD) to Color(0xFF856404)     // Soft Yellow
        "REJECTED" -> Color(0xFFF8D7DA) to Color(0xFF721C24)    // Soft Red
        "CANCELLED" -> Color(0xFFE2E3E5) to Color(0xFF383D41)   // Soft Gray
        else -> Color(0xFFE2E3E5) to Color(0xFF383D41)
    }

    Text(
        text = status,
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}