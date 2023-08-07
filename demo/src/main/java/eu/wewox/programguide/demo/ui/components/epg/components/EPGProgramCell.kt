package eu.wewox.programguide.demo.ui.components.epg.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import eu.wewox.programguide.demo.data.Program
import eu.wewox.programguide.demo.data.formatTime
import eu.wewox.programguide.demo.extensions.epgBackground
import eu.wewox.programguide.demo.extensions.epgBorder
import eu.wewox.programguide.demo.extensions.epgPadding


@Composable
fun EPGProgramCell(
    program: Program,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = Color.Transparent,
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .epgPadding()
                .epgBackground(isSelected = isSelected)
                .epgBorder(isHighlight = isHighlight)
                .padding(4.dp)
        ) {
            Text(
                text = program.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = formatTime(program.start, program.end),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}