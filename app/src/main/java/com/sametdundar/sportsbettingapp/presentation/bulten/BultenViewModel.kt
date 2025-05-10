package com.sametdundar.sportsbettingapp.presentation.bulten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BultenViewModel : ViewModel() {
    private val _state = MutableStateFlow(BultenState())
    val state: StateFlow<BultenState> = _state

    fun onEvent(event: BultenEvent) {
        when (event) {
            is BultenEvent.Refresh -> {
                // Burada veri çekme işlemleri yapılabilir
            }
        }
    }
} 