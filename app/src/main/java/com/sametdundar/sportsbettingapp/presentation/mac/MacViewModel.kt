package com.sametdundar.sportsbettingapp.presentation.mac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.domain.model.Coupon
import com.sametdundar.sportsbettingapp.domain.usecase.SaveCouponUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.basket.AddBetToBasketUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.basket.RemoveBetFromBasketUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.basket.ClearBasketUseCase
import com.sametdundar.sportsbettingapp.domain.repository.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

@HiltViewModel
class MacViewModel @Inject constructor(
    private val addBetToBasketUseCase: AddBetToBasketUseCase,
    private val removeBetFromBasketUseCase: RemoveBetFromBasketUseCase,
    private val clearBasketUseCase: ClearBasketUseCase,
    private val saveCouponUseCase: SaveCouponUseCase,
    private val basketRepository: BasketRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MacState())
    val state: StateFlow<MacState> = _state

    init {
        onEvent(MacEvent.LoadSelectedBets)
    }

    fun onEvent(event: MacEvent, onAllBetsCleared: (() -> Unit)? = null) {
        when (event) {
            is MacEvent.LoadSelectedBets -> {
                viewModelScope.launch {
                    basketRepository.selectedBets.collectLatest { bets ->
                        val oran = bets.mapNotNull { it.odd }.fold(1.0) { acc, d -> acc * d }
                        val kuponBedeli = _state.value.kuponBedeli.toDoubleOrNull() ?: 50.0
                        val maksKazanc = if (bets.isNotEmpty()) oran * kuponBedeli else 0.0
                        _state.value = _state.value.copy(
                            selectedBets = bets,
                            toplamOran = oran,
                            maksKazanc = maksKazanc
                        )
                    }
                }
            }
            is MacEvent.KuponBedeliChanged -> {
                val value = event.value.filter { it.isDigit() }
                val bedel = value.toDoubleOrNull() ?: 0.0
                val minBedel = max(50.0, bedel)
                val oran = _state.value.selectedBets.mapNotNull { it.odd }.fold(1.0) { acc, d -> acc * d }
                val maksKazanc = if (_state.value.selectedBets.isNotEmpty()) oran * minBedel else 0.0
                _state.value = _state.value.copy(
                    kuponBedeli = if (value.isEmpty()) "50" else minBedel.toInt().toString(),
                    toplamOran = oran,
                    maksKazanc = maksKazanc
                )
            }
            is MacEvent.ShowDeleteDialog -> {
                _state.value = _state.value.copy(showDeleteDialog = true)
            }
            is MacEvent.HideDeleteDialog -> {
                _state.value = _state.value.copy(showDeleteDialog = false)
            }
            is MacEvent.DeleteAllBets -> {
                clearBasketUseCase()
                _state.value = _state.value.copy(showDeleteDialog = false)
            }
            is MacEvent.SaveCoupon -> {
                viewModelScope.launch {
                    val bets = _state.value.selectedBets
                    if (bets.isNotEmpty()) {
                        val kuponBedeli = _state.value.kuponBedeli.toDoubleOrNull() ?: 50.0
                        val oran = _state.value.toplamOran
                        val maksKazanc = _state.value.maksKazanc
                        val coupon = Coupon(
                            kuponBedeli = kuponBedeli,
                            toplamOran = oran,
                            maksKazanc = maksKazanc,
                            bets = bets
                        )
                        saveCouponUseCase(coupon)
                        clearBasketUseCase()
                        _state.value = MacState()
                        onAllBetsCleared?.invoke()
                    }
                }
            }
            is MacEvent.ShowSaveCouponDialog -> {
                _state.value = _state.value.copy(showSaveCouponDialog = true)
            }
            is MacEvent.HideSaveCouponDialog -> {
                _state.value = _state.value.copy(showSaveCouponDialog = false)
            }
        }
    }
}