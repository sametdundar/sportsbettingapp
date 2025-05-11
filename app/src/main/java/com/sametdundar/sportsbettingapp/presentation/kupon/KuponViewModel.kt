package com.sametdundar.sportsbettingapp.presentation.kupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.domain.usecase.GetAllCouponsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class KuponViewModel @Inject constructor(
    private val getAllCouponsUseCase: GetAllCouponsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(KuponState())
    val state: StateFlow<KuponState> = _state

    init {
        onEvent(KuponEvent.LoadCoupons)
    }

    fun onEvent(event: KuponEvent) {
        when (event) {
            is KuponEvent.LoadCoupons -> {
                viewModelScope.launch {
                    getAllCouponsUseCase().collectLatest { coupons ->
                        _state.value = _state.value.copy(coupons = coupons)
                    }
                }
            }
        }
    }
} 