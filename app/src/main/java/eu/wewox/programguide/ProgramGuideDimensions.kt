package eu.wewox.programguide

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

data class ProgramGuideDimensions(
    val timelineHourWidth: Dp,
    val timelineHeight: Dp,
    val channelWidth: Dp,
    val channelHeight: Dp,
)

context(Density)
internal fun ProgramGuideDimensions.roundToPx(): ProgramGuidePxDimensions =
    ProgramGuidePxDimensions(
        timelineHourWidth = timelineHourWidth.roundToPx(),
        timelineHeight = timelineHeight.roundToPx(),
        channelWidth = channelWidth.roundToPx(),
        channelHeight = channelHeight.roundToPx(),
    )

internal data class ProgramGuidePxDimensions(
    val timelineHourWidth: Int,
    val timelineHeight: Int,
    val channelWidth: Int,
    val channelHeight: Int,
)