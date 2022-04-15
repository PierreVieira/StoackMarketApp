package com.example.stockmarket.presentation.company_listings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBoxComponent(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = "Search...")
        },
        maxLines = 1,
        singleLine = true
    )
}