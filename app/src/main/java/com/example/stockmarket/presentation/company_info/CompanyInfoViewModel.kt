package com.example.stockmarket.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository,
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }
            catchCompanyInfo(companyInfoResult)
            catchIntradayInfo(intradayInfoResult)
        }
    }

    private suspend fun catchCompanyInfo(companyInfoResult: Deferred<Resource<CompanyInfo>>) {
        when (val result = companyInfoResult.await()) {
            is Resource.Success -> {
                state = state.copy(
                    company = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message,
                    company = null
                )
            }
            is Resource.Loading -> {
                state = state.copy(isLoading = true)
            }
        }
    }

    private suspend fun catchIntradayInfo(intradayInfoResult: Deferred<Resource<List<IntradayInfo>>>) {
        when (val result = intradayInfoResult.await()) {
            is Resource.Success -> {
                state = state.copy(
                    stockInfos = result.data ?: emptyList(),
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message,
                    company = null
                )
            }
            is Resource.Loading -> {
                state = state.copy(isLoading = true)
            }
        }
    }
}