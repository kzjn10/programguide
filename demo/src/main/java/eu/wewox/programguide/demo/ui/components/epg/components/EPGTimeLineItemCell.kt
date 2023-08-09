package eu.wewox.programguide.demo.ui.components.epg.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wewox.programguide.demo.data.formatTime
import eu.wewox.programguide.demo.extensions.epgBackground
import eu.wewox.programguide.demo.extensions.epgPadding

@Composable
fun EPGTimeLineItemCell(hour: Float, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .epgPadding()
                .epgBackground()
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = formatTime(hour),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

    }
}