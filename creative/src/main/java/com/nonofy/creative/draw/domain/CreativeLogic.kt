package com.nonofy.creative.draw.domain

import com.nonofy.creative.draw.domain.performers.UpdatePixelAtPositionPerformer
import com.nonofy.creative.draw.presentation.CreativeScreenState
import com.nonofy.ui.components.grid.GridState
import com.nonofy.utils.Feature
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class CreativeLogic @AssistedInject constructor(
    reducer: CreativeReducer,
    private val updatePixelAtPositionPerformer: UpdatePixelAtPositionPerformer,
    @Assisted gridSize: Int
) : Feature<CreativeScreenState, CreativeEvent, CreativeEffect>(
    actionPerformers = arrayOf(
        updatePixelAtPositionPerformer.flow
    ),
    reducer = reducer,
    scope = CoroutineScope(Dispatchers.IO),
    initialValue = CreativeScreenState(
        GridState.empty(gridSize)
    )
) {
    @AssistedFactory
    interface Factory {
        fun create(
            gridSize: Int
        ): CreativeLogic
    }

    override fun onEvent(event: CreativeEvent) = when (event) {
        is CreativeEvent.OnClickPixel -> updatePixelAtPositionPerformer.invoke(
            UpdatePixelAtPositionPerformer.Params(
                model.value.gridState.pixels[event.position],
                event.position
            )
        )
    }
}