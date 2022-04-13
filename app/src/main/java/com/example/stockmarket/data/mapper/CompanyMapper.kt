package com.example.stockmarket.data.mapper

import com.example.stockmarket.data.local.CompanyListingEntity
import com.example.stockmarket.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing = CompanyListing(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity = CompanyListingEntity(
    name = name,
    symbol = symbol,
    exchange = exchange
)