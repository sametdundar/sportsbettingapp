package com.sametdundar.sportsbettingapp.presentation.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.data.repository.OddsRepositoryImpl
import com.sametdundar.sportsbettingapp.domain.usecase.GetEventOddsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventOddsUseCase: GetEventOddsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EventDetailState())
    val state: StateFlow<EventDetailState> = _state

    fun onEvent(event: EventDetailEvent) {
        when (event) {
            is EventDetailEvent.LoadOdds -> {
                loadOdds(event.sportKey, event.eventId)
            }
        }
    }

    private fun loadOdds(sportKey: String, eventId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val odds = getEventOddsUseCase(sportKey, eventId)
                _state.value = _state.value.copy(odds = odds, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage ?: "Bilinmeyen hata")
            }
        }
    }
} 