package com.nonofy.game.impl.com.nonofy.game.impl.domain.usecase

import com.nonofy.game.impl.domain.GameRepository
import com.nonofy.game.impl.domain.models.Nonogram
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadSavedGameUseCase @Inject constructor(
    private val repository: GameRepository
) {
    fun run(): Flow<Nonogram> =
        repository.loadSavedGame()
}