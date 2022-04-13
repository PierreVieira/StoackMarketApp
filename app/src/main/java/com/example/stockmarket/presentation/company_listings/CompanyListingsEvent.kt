package com.example.stockmarket.presentation.company_listings

sealed interface CompanyListingsEvent {
    object Refresh : CompanyListingsEvent
    data class OnSearchQueryChange(val query: String) : CompanyListingsEvent
}