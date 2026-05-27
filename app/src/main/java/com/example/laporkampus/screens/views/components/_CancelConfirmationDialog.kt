package com.example.laporkampus.screens.views.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CancelConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Triggered when tapping outside the dialog
        title = {
            Text(
                text = "Cancel Report",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to cancel this report? This action cannot be undone."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Yes, Cancel",
                    color = MaterialTheme.colorScheme.error // Using red/error color to indicate a destructive action
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "No, Keep It")
            }
        }
    )
}