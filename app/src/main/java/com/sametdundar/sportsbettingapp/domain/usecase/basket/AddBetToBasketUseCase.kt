package com.sametdundar.sportsbettingapp.domain.usecase.basket

import com.sametdundar.sportsbettingapp.domain.model.SelectedBet
import com.sametdundar.sportsbettingapp.domain.repository.BasketRepository
import javax.inject.Inject

class AddBetToBasketUseCase @Inject constructor(
    private val basketRepository: BasketRepository
) {
    operator fun invoke(bet: SelectedBet) = basketRepository.addBet(bet)
} 