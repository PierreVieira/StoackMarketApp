package com.example.stockmarket.data.repository

import com.example.stockmarket.data.local.StockDatabase
import com.example.stockmarket.data.mapper.toCompanyListing
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.model.CompanyListing
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
    val api: StockApi,
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

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data"))
        } catch (e: HttpException) {
            emit(Resource.Error("Couldn't load data"))
        }
    }

}