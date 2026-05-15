package com.reshme.nammapride.presentation.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.model.SilkVariety
import com.reshme.nammapride.domain.repository.ReshmeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MarketplaceState(
    val varieties: List<SilkVariety> = emptyList(),
    val isLoading: Boolean = true
)

class MarketplaceViewModel(private val repository: ReshmeRepository) : ViewModel() {

    val state: StateFlow<MarketplaceState> = repository.observeSilkPrices()
        .map { varieties ->
            MarketplaceState(varieties = varieties, isLoading = false)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            MarketplaceState()
        )
}
