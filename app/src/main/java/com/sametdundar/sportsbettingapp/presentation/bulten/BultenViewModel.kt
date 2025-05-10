package com.sametdundar.sportsbettingapp.presentation.bulten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BultenViewModel @Inject constructor(
    private val oddsRepository: OddsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BultenState())
    val state: StateFlow<BultenState> = _state

    init {
        onEvent(BultenEvent.LoadSports)
    }

    fun onEvent(event: BultenEvent) {
        when (event) {
            is BultenEvent.Refresh, is BultenEvent.LoadSports -> {
                loadSports()
            }
            is BultenEvent.SelectGroup -> {
                _state.value = _state.value.copy(selectedGroup = event.group)
            }
            is BultenEvent.ShowApiKeyDialog -> {
                val currentKey = oddsRepository.getApiKey()
                _state.value = _state.value.copy(showApiKeyDialog = true, currentApiKey = currentKey)
            }
            is BultenEvent.HideApiKeyDialog -> {
                _state.value = _state.value.copy(showApiKeyDialog = false)
            }
            is BultenEvent.ChangeApiKey -> {
                oddsRepository.setApiKey(event.newKey)
                _state.value = _state.value.copy(showApiKeyDialog = false, currentApiKey = event.newKey)
                onEvent(BultenEvent.LoadSports)
            }
        }
    }

    private fun loadSports() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val sports = oddsRepository.getSports()
                val groups = sports.map { it.group }.distinct()
                val selectedGroup = groups.firstOrNull()
                _state.value = _state.value.copy(
                    sports = sports,
                    groups = groups,
                    selectedGroup = selectedGroup,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Bilinmeyen hata"
                )
            }
        }
    }
} 