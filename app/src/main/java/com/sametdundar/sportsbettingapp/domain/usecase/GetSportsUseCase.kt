package com.sametdundar.sportsbettingapp.domain.usecase

import com.sametdundar.sportsbettingapp.domain.model.Sport
import com.sametdundar.sportsbettingapp.domain.repository.OddsRepository

class GetSportsUseCase(val repository: OddsRepository) {
    suspend operator fun invoke(): List<Sport> = repository.getSports()
} 