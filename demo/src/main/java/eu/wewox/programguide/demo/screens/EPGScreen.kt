package eu.wewox.programguide.demo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import eu.wewox.programguide.demo.Example
import eu.wewox.programguide.demo.data.createEPGPrograms
import eu.wewox.programguide.demo.data.generateChannel
import eu.wewox.programguide.demo.data.generateTimeline
import eu.wewox.programguide.demo.extensions.currentHourAndMinute
import eu.wewox.programguide.demo.extensions.currentHourInFloat
import eu.wewox.programguide.demo.ui.components.TopBar
import eu.wewox.programguide.demo.ui.components.epg.EPGSettings
import eu.wewox.programguide.demo.ui.components.epg.ElectronicProgramGuide
import eu.wewox.programguide.demo.ui.theme.SpacingSmall
import eu.wewox.programguide.rememberProgramGuideState
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Example how program guide state could be used.
 */
@Composable
fun EPGScreen() {
    Scaffold(
        topBar = { TopBar(Example.ProgramGuideState.label) }
    ) { padding ->
        val calendar = Calendar.getInstance()
        val timelineStep = 0.5f
        val timeline = generateTimeline(startTime = 0.0f, endTime = 23.5f, step = timelineStep)
        val channels by remember { mutableStateOf(generateChannel()) }
        var programs by remember { mutableStateOf(createEPGPrograms(channels.size, timeline)) }
        val currentHour by remember {
            mutableStateOf(calendar.currentHourInFloat())
        }
        val currentHourAndMinute by remember {
            mutableStateOf(calendar.currentHourAndMinute())
        }
        val scope = rememberCoroutineScope()
        val state = rememberProgramGuideState(
            initialOffset = {
                val x = getCurrentTimePosition()
                Offset(x, 0f)
            }
        )

        val buttonVisibilityThreshold = LocalDensity.current.run { 48.dp.toPx() }
        val showPrev by remember {
            derivedStateOf {
                val translate = state.minaBoxState.translate
                if (translate == null) {
                    false
                } else {
                    translate.x < buttonVisibilityThreshold
                }
            }
        }
        val showNext by remember {
            derivedStateOf {
                val translate = state.minaBoxState.translate
                if (translate == null) {
                    false
                } else {
                    translate.maxX - translate.x < buttonVisibilityThreshold
                }
            }
        }
        val settings by remember { mutableStateOf(EPGSettings()) }
        var selectedChannel by remember { mutableStateOf(channels.first()) }
        var selectedProgram by remember { mutableStateOf(programs.first()) }
        var ctpOnParent by remember { mutableStateOf(Offset.Zero) }

        val ctlStartPosition =
            LocalDensity.current.run { (settings.channelWidth - settings.currentTimeWidth).toPx() }
        val ctlEndPosition = LocalContext.current.resources.displayMetrics.widthPixels - 1
        val spacer = LocalDensity.current.run { 4.dp.toPx() }

        val liveNow by remember {
            derivedStateOf {
                val translate = state.minaBoxState.translate
                if (translate == null) {
                    false
                } else {
                    val ctp = state.positionProvider.getCurrentTimePosition()
                    val limitOfCTLinParent =
                        translate.x + (ctlEndPosition - ctlStartPosition) / 2 + spacer

                    return@derivedStateOf if (ctpOnParent.x >= ctlEndPosition)
                        true
                    else if (ctpOnParent.x <= ctlStartPosition)
                        true
                    else ctp >= limitOfCTLinParent
                }
            }
        }

        val indicatorVisible by remember { mutableStateOf(true) }

        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                TextButton(onClick = {
                    scope.launch {
                        state.animateToCurrentTime()
                    }
                }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.PlayArrow, contentDescription = "",
                            tint = if (liveNow) MaterialTheme.colorScheme.primary else Color.Red
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Live Now")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "")
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                ElectronicProgramGuide(
                    state = state,
                    channels = channels,
                    timeline = timeline,
                    programs = programs,
                    settings = settings,
                    currentTime = currentHour,
                    timelineLabel = currentHourAndMinute,
                    timelineStep = timelineStep,
                    selectedChannel = selectedChannel,
                    onSelectChannelChanged = {
                        selectedChannel = it
                    },
                    selectedProgram = selectedProgram,
                    onSelectProgramChanged = {
                        selectedProgram = it
                    },
                    indicationInvisible = indicatorVisible,
                    onTimeLineOffsetChanged = { offset ->
                        ctpOnParent = offset
                    }
                )

                ControlButton(
                    visible = showPrev,
                    imageVector = Icons.Default.ArrowBack,
                    onClick = { programs = createEPGPrograms(channels.size, timeline) },
                    modifier = Modifier.align(Alignment.CenterStart),
                )

                ControlButton(
                    visible = showNext,
                    imageVector = Icons.Default.ArrowForward,
                    onClick = { programs = createEPGPrograms(channels.size, timeline) },
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
    }
}

@Composable
private fun ControlButton(
    visible: Boolean,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.padding(SpacingSmall),
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null
            )
        }
    }
}
