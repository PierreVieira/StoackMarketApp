package com.example.stockmarket.presentation.company_listings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarket.presentation.company_listings.components.SearchBoxComponent
import com.example.stockmarket.presentation.company_listings.components.StockList
import com.example.stockmarket.presentation.destinations.CompanyInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBoxComponent(
            modifier = Modifier.padding(16.dp),
            searchQuery = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(CompanyListingsEvent.OnSearchQueryChange(it))
            }
        )
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            StockList(
                isRefreshing = state.isRefreshing,
                companies = state.companies,
                onRefresh = {
                    viewModel.onEvent(CompanyListingsEvent.Refresh)
                },
                onItemClick = { symbol ->
                    navigator.navigate(CompanyInfoScreenDestination(symbol))
                }
            )
        }
    }
}

