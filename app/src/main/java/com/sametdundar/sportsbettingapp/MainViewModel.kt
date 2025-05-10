package com.sametdundar.sportsbettingapp

import androidx.lifecycle.ViewModel
import com.sametdundar.sportsbettingapp.di.BasketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    val basketManager: BasketManager
) : ViewModel() {
    val selectedBets: StateFlow<List<com.sametdundar.sportsbettingapp.domain.model.SelectedBet>> = basketManager.selectedBets
} 