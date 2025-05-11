package com.sametdundar.sportsbettingapp.domain.usecase.basket

import com.sametdundar.sportsbettingapp.domain.repository.BasketRepository
import javax.inject.Inject

class ClearBasketUseCase @Inject constructor(
    private val basketRepository: BasketRepository
) {
    operator fun invoke() = basketRepository.clearBets()
} 