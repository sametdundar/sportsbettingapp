package com.sametdundar.sportsbettingapp.presentation.bulten

import com.sametdundar.sportsbettingapp.domain.model.Sport

data class BultenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sports: List<Sport> = emptyList(),
    val selectedGroup: String? = null,
    val groups: List<String> = emptyList(),
    val showApiKeyDialog: Boolean = false,
    val currentApiKey: String = ""
    // Buraya bülten verileri eklenecek
) 