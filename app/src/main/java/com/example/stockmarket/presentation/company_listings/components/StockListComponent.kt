package com.example.stockmarket.presentation.company_listings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun StockList(
    isRefreshing: Boolean,
    companies: List<CompanyListing>,
    onRefresh: () -> Unit,
    onItemClick: (String) -> Unit,
) = SwipeRefresh(
    state = rememberSwipeRefreshState(
        isRefreshing = isRefreshing
    ),
    onRefresh = onRefresh
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(companies.size) { index ->
            val company = companies[index]
            CompanyItemComponent(
                company = company,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(company.symbol) }
                    .padding(16.dp)
            )
            if (index < companies.size) {
                Divider(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    )
                )
            }
        }
    }
}