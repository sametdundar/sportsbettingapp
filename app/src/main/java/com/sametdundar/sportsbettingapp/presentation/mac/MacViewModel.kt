package com.sametdundar.sportsbettingapp.presentation.mac

import androidx.lifecycle.ViewModel
import com.sametdundar.sportsbettingapp.di.BasketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MacViewModel @Inject constructor(
    val basketManager: BasketManager
) : ViewModel()