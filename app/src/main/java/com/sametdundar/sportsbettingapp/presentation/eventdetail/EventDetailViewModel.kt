package com.sametdundar.sportsbettingapp.presentation.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.data.repository.OddsRepositoryImpl
import com.sametdundar.sportsbettingapp.domain.usecase.GetEventOddsUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.basket.AddBetToBasketUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.basket.RemoveBetFromBasketUseCase
import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventOddsUseCase: GetEventOddsUseCase,
    private val addBetToBasketUseCase: AddBetToBasketUseCase,
    private val removeBetFromBasketUseCase: RemoveBetFromBasketUseCase
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

    fun addBet(bet: SelectedBet) = addBetToBasketUseCase(bet)
    fun removeBet(bet: SelectedBet) = removeBetFromBasketUseCase(bet)

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