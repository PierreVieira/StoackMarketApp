package com.example.stockmarket.data.repository

import com.example.stockmarket.data.csv.CsvParser
import com.example.stockmarket.data.csv.IntradayInfoParser
import com.example.stockmarket.data.local.StockDatabase
import com.example.stockmarket.data.mapper.toCompanyInfo
import com.example.stockmarket.data.mapper.toCompanyListing
import com.example.stockmarket.data.mapper.toCompanyListingEntity
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.util.Resource
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val companyListingsParser: CsvParser<CompanyListing>,
    private val api: StockApi,
    private val intradayInfoParser: CsvParser<IntradayInfo>,
    database: StockDatabase,
) : StockRepository {

    private val dao = database.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<CompanyListing>>> = flow {
        emit(value = Resource.Loading(true))
        val localListing = dao.searchCompanyListing(query)
        emit(Resource.Success(
            data = localListing.map { it.toCompanyListing() }
        ))
        val isDatabaseEmpty = localListing.isEmpty() && query.isBlank()
        val shouldJustLoadFromCache = !isDatabaseEmpty && !fetchFromRemote

        if (shouldJustLoadFromCache) {
            emit(Resource.Loading(false))
            return@flow
        }
        val remoteListings = try {
            val response = api.getListings()
            companyListingsParser.parse(response.byteStream())
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data"))
            null
        } catch (e: HttpException) {
            emit(Resource.Error("Couldn't load data"))
            null
        }
        remoteListings?.let { listings ->
            dao.clearCompanyListings()
            dao.insertCompanyListings(
                listings.map { it.toCompanyListingEntity() }
            )
            emit(
                Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                )
            )
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> = try {
        val response = api.getIntradayInfo(symbol)
        val results = intradayInfoParser.parse(response.byteStream())
        Resource.Success(results)
    } catch (e: IOException) {
        Resource.Error("Couldn't load data")
    } catch (e: HttpException) {
        Resource.Error("Couldn't load data")
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> = try {
        val result = api.getCompanyInfo(symbol)
        Resource.Success(result.toCompanyInfo())
    } catch (e: IOException) {
        Resource.Error("Couldn't load data")
    } catch (e: HttpException) {
        Resource.Error("Couldn't load data")
    }

}