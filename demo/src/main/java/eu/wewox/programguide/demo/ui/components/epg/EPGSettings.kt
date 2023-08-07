package eu.wewox.programguide.demo.ui.components.epg

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class EPGSettings(
    val timelineHourWidth: Dp = 400.dp,
    val timelineHeight: Dp = 50.dp,
    val channelWidth: Dp = 128.dp,
    val channelHeight: Dp = 80.dp,
    val currentTimeWidth: Float = 100f,
)