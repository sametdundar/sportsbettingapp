package com.sametdundar.sportsbettingapp.presentation.mac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.di.BasketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max
import com.sametdundar.sportsbettingapp.data.local.CouponDao
import com.sametdundar.sportsbettingapp.domain.model.Coupon

@HiltViewModel
class MacViewModel @Inject constructor(
    private val basketManager: BasketManager,
    private val couponDao: CouponDao
) : ViewModel() {
    private val _state = MutableStateFlow(MacState())
    val state: StateFlow<MacState> = _state

    init {
        onEvent(MacEvent.LoadSelectedBets)
    }

    fun onEvent(event: MacEvent) {
        when (event) {
            is MacEvent.LoadSelectedBets -> {
                viewModelScope.launch {
                    basketManager.selectedBets.collectLatest { bets ->
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
                basketManager.clearBets()
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
                        couponDao.insertCoupon(coupon)
                        basketManager.clearBets()
                        _state.value = MacState() // state'i sıfırla
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