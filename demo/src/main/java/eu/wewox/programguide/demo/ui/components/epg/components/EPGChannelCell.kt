package eu.wewox.programguide.demo.ui.components.epg.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wewox.programguide.demo.extensions.epgBackground
import eu.wewox.programguide.demo.extensions.epgPadding

/**
 * Single channel cell in program guide.
 *
 * @param index The channel index.
 * @param modifier The modifier instance for the root composable.
 * @param onClick Callback to be called when the surface is clicked.
 */
@Composable
fun EPGChannelCell(
    index: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .epgPadding()
                .epgBackground(isSelected = isSelected),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ch #$index",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}