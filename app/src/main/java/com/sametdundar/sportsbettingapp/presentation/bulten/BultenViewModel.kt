package com.sametdundar.sportsbettingapp.presentation.bulten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametdundar.sportsbettingapp.domain.usecase.GetSportsUseCase
import com.sametdundar.sportsbettingapp.domain.usecase.GetOddsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BultenViewModel @Inject constructor(
    private val getSportsUseCase: GetSportsUseCase,
    private val getOddsUseCase: GetOddsUseCase
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
                val filteredSports = _state.value.sports.filter { it.group == event.group }
                val firstSport = filteredSports.firstOrNull()
                _state.value = _state.value.copy(
                    selectedGroup = event.group,
                    selectedSportIndex = 0
                )
                if (firstSport != null) {
                    onEvent(BultenEvent.LoadOdds(firstSport.key))
                } else {
                    _state.value = _state.value.copy(odds = emptyList())
                }
            }
            is BultenEvent.ShowApiKeyDialog -> {
                val currentKey = getSportsUseCase.repository.getApiKey()
                _state.value = _state.value.copy(showApiKeyDialog = true, currentApiKey = currentKey)
            }
            is BultenEvent.HideApiKeyDialog -> {
                _state.value = _state.value.copy(showApiKeyDialog = false)
            }
            is BultenEvent.ChangeApiKey -> {
                getSportsUseCase.repository.setApiKey(event.newKey)
                _state.value = _state.value.copy(showApiKeyDialog = false, currentApiKey = event.newKey)
                onEvent(BultenEvent.LoadSports)
            }
            is BultenEvent.SelectSport -> {
                val filteredSports = _state.value.sports.filter { it.group == _state.value.selectedGroup }
                val selectedSport = filteredSports.getOrNull(event.index)
                _state.value = _state.value.copy(selectedSportIndex = event.index)
                selectedSport?.let {
                    onEvent(BultenEvent.LoadOdds(it.key))
                }
            }
            is BultenEvent.LoadOdds -> {
                loadOdds(event.sportKey)
            }
        }
    }

    private fun loadSports() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val sports = getSportsUseCase()
                val groups = sports.map { it.group }.distinct()
                val selectedGroup = groups.firstOrNull()
                val filteredSports = sports.filter { it.group == selectedGroup }
                val firstSport = filteredSports.firstOrNull()
                _state.value = _state.value.copy(
                    sports = sports,
                    groups = groups,
                    selectedGroup = selectedGroup,
                    selectedSportIndex = 0,
                    isLoading = false
                )
                if (firstSport != null) {
                    onEvent(BultenEvent.LoadOdds(firstSport.key))
                } else {
                    _state.value = _state.value.copy(odds = emptyList())
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Bilinmeyen hata"
                )
            }
        }
    }

    private fun loadOdds(sportKey: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val odds = getOddsUseCase(sportKey, "us", "h2h")
                _state.value = _state.value.copy(odds = odds, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage ?: "Bilinmeyen hata")
            }
        }
    }
} 