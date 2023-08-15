package eu.wewox.programguide.demo.ui.components.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.wewox.programguide.ProgramGuide
import eu.wewox.programguide.ProgramGuideDefaults
import eu.wewox.programguide.ProgramGuideDimensions
import eu.wewox.programguide.ProgramGuideItem
import eu.wewox.programguide.ProgramGuideState
import eu.wewox.programguide.demo.data.Channel
import eu.wewox.programguide.demo.data.Program
import eu.wewox.programguide.demo.ui.components.CurrentTimeLine
import eu.wewox.programguide.demo.ui.components.epg.components.EPGChannelCell
import eu.wewox.programguide.demo.ui.components.epg.components.EPGCurrentTimeLine
import eu.wewox.programguide.demo.ui.components.epg.components.EPGProgramCell
import eu.wewox.programguide.demo.ui.components.epg.components.EPGTimeLineItemCell
import eu.wewox.programguide.demo.ui.components.epg.components.EPGTopCorner

@Composable
fun ElectronicProgramGuide(
    modifier: Modifier = Modifier,
    state: ProgramGuideState,
    channels: List<Channel>,
    timeline: List<Float>,
    programs: List<Program>,
    settings: EPGSettings,
    currentTime: Float = 12.5f,
    timelineLabel: String = "",
    timelineStep: Float = 0.5f,
    selectedChannel: Channel? = null,
    selectedProgram: Program? = null,
    onSelectProgramChanged: (Program) -> Unit,
    onSelectChannelChanged: (Channel) -> Unit,
    onTimeLineOffsetChanged: ((Offset) -> Unit)? = null,
    indicationInvisible: Boolean = false,
) {
    var offsetX by remember { mutableStateOf<Float>(state.minaBoxState.translate?.x ?: 0.0f) }

    LaunchedEffect(state) {
        snapshotFlow { state.minaBoxState.translate }
            .collect { translate ->
                translate?.let {
                    offsetX = it.x
                }
            }
    }

    LaunchedEffect(UInt) {
        state.minaBoxState.translate?.let {
            offsetX = it.x
        }
    }

    ProgramGuide(
        state = state,
        dimensions = dimensions(
            timelineHourWidth = settings.timelineHourWidth,
            timelineHeight = settings.timelineHeight,
            channelWidth = settings.channelWidth,
            channelHeight = settings.channelHeight,
            currentTimeWidth = settings.currentTimeWidth,
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        guideStartHour = timeline.first()

        programs(
            items = programs,
            layoutInfo = {
                ProgramGuideItem.Program(
                    channelIndex = it.channel,
                    startHour = it.start,
                    endHour = it.end,
                )
            },
            itemContent = { program, index ->
                val offsetXStart = state.minaBoxState.positionProvider.getItemStartX(index)
                val offsetXEnd =
                    offsetXStart + state.minaBoxState.positionProvider.getItemSize(index).width
                val itemOffsetX =
                    (offsetX + with(LocalDensity.current) { settings.channelWidth.toPx() }) - offsetXStart
                val itemPadding =
                    if (itemOffsetX < 0.0f || offsetX > offsetXEnd) 0.0f else itemOffsetX
                val paddingInDp = with(LocalDensity.current) { itemPadding.toDp() }

                EPGProgramCell(
                    program = program,
                    isHighlight = program == selectedProgram,
                    isSelected = program.channel == selectedChannel?.index,
                    onClick = {
                        onSelectChannelChanged(channels[program.channel])
                        onSelectProgramChanged(program)
                    },
                    modifier = modifier.padding(start = paddingInDp)
                )
            },
        )

        channels(
            count = channels.size,
            layoutInfo = {
                ProgramGuideItem.Channel(
                    index = it
                )
            },
            itemContent = {
                EPGChannelCell(
                    modifier = Modifier.zIndex(4f),
                    index = it,
                    isSelected = channels[it] == selectedChannel,
                    onClick = {
                        onSelectChannelChanged(channels[it])
                    },
                )
            },
        )

        timeline(
            count = timeline.count(),
            layoutInfo = {
                val start = timeline.toList()[it]
                ProgramGuideItem.Timeline(
                    startHour = start,
                    endHour = start + timelineStep
                )
            },
            itemContent = {
                EPGTimeLineItemCell(hour = timeline.toList()[it])
            },
        )

        currentTime(
            key = { "currentTime"},
            layoutInfo = { ProgramGuideItem.CurrentTime(currentTime) },
            itemContent = {
                EPGCurrentTimeLine(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (onTimeLineOffsetChanged != null) {
                            onTimeLineOffsetChanged(coordinates.positionInParent())
                        }
                    }.zIndex(3f),
                    label = timelineLabel
                )
            },
        )

        topCorner(
            key = { "topCorner"},
            itemContent = {
                EPGTopCorner(modifier = Modifier.zIndex(4f), indicationInvisible = indicationInvisible)
            },
        )
    }
}


private fun dimensions(
    timelineHourWidth: Dp = 200.dp,
    timelineHeight: Dp = 50.dp,
    channelWidth: Dp = 128.dp,
    channelHeight: Dp = 80.dp,
    currentTimeWidth: Dp = 200.dp,
): ProgramGuideDimensions =
    ProgramGuideDefaults.dimensions.copy(
        timelineHourWidth = timelineHourWidth,
        timelineHeight = timelineHeight,
        channelWidth = channelWidth,
        channelHeight = channelHeight,
        currentTimeWidth = currentTimeWidth
    )

