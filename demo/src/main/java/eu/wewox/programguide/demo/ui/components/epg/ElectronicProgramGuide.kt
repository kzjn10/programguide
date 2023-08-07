package eu.wewox.programguide.demo.ui.components.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.wewox.programguide.ProgramGuide
import eu.wewox.programguide.ProgramGuideDefaults
import eu.wewox.programguide.ProgramGuideDimensions
import eu.wewox.programguide.ProgramGuideItem
import eu.wewox.programguide.ProgramGuideState
import eu.wewox.programguide.demo.data.Channel
import eu.wewox.programguide.demo.data.Program
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
    indicationInvisible: Boolean = false,
) {
    ProgramGuide(
        state = state,
        dimensions = dimensions(
            timelineHourWidth = settings.timelineHourWidth,
            timelineHeight = settings.timelineHeight,
            channelWidth = settings.channelWidth,
            channelHeight = settings.channelHeight,
            currentTimeWidth = settings.currentTimeWidth.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Black),
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
            itemContent = {
                EPGProgramCell(
                    program = it,
                    isHighlight = it == selectedProgram,
                    isSelected = it.channel == selectedChannel?.index,
                    onClick = {
                        onSelectChannelChanged(channels[it.channel])
                        onSelectProgramChanged(it)
                    },
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
            layoutInfo = { ProgramGuideItem.CurrentTime(currentTime) },
            itemContent = {
                EPGCurrentTimeLine(
                    label = timelineLabel
                )
            },
        )

        topCorner(
            itemContent = {
                EPGTopCorner(indicationInvisible = indicationInvisible)
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

