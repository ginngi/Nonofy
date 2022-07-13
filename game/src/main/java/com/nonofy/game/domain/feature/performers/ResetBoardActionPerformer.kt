package com.nonofy.game.domain.feature.performers


import com.nonofy.game.domain.feature.InGameEffect
import com.nonofy.game.domain.models.Difficulty
import com.nonofy.game.domain.models.Grid
import com.nonofy.game.domain.models.Header
import com.nonofy.game.domain.models.Nonogram
import com.nonofy.game.domain.models.Pixel
import com.nonofy.utils.Performer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetBoardActionPerformer @Inject constructor(
) : Performer<ResetBoardActionPerformer.Params, InGameEffect.GameLoaded>() {

    override fun createObservable(params: Params): Flow<InGameEffect.GameLoaded> = flow {
        val verticalHeaders = params.nonogram.verticalHeaders.map {
            if (it.filledPixels > 0) {
                it.copy(isCompleted = false)
            } else it
        }

        val horizontalHeaders = params.nonogram.horizontalHeaders.map {
            if (it.filledPixels > 0) {
                it.copy(isCompleted = false)
            } else it
        }

        val grid = createGrid(verticalHeaders, horizontalHeaders, params.nonogram.difficulty)

        emit(
            InGameEffect.GameLoaded(
                nonogram = params.nonogram.copy(
                    numErrors = 0,
                    grid = grid,
                    verticalHeaders = verticalHeaders,
                    horizontalHeaders = horizontalHeaders
                )
            )
        )
    }

    private fun createGrid(
        verticalHeaders: List<Header>,
        horizontalHeaders: List<Header>,
        difficulty: Difficulty
    ): Grid {
        val newBoard: MutableList<MutableList<Pixel>> =
            MutableList(difficulty.size) { MutableList(difficulty.size) { Pixel.EMPTY } }

        verticalHeaders.forEachIndexed { index, header ->
            if (header.isCompleted) {
                newBoard.forEach {
                    if (it[index] == Pixel.EMPTY) {
                        it[index] = Pixel.ERROR
                    }
                }
            }
        }

        horizontalHeaders.forEachIndexed { index, header ->
            if (header.isCompleted) {
                newBoard[index].forEachIndexed { i, pixel ->
                    if (pixel == Pixel.EMPTY) {
                        newBoard[index][i] = Pixel.ERROR
                    }
                }
            }
        }

        return Grid(
            pixels = newBoard,
            numFilledPixels = 0,
            size = difficulty.size
        )
    }

    data class Params(val nonogram: Nonogram)
}