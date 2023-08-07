package eu.wewox.programguide.demo.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.epgBackground(
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    isSelected: Boolean = false,
    roundedCorner: Dp = 4.dp,
): Modifier = composed {
    return@composed this.background(
        color = if (isSelected) selectedColor else color, shape = RoundedCornerShape(roundedCorner)
    )
}

fun Modifier.epgPadding(
    start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 4.dp, bottom: Dp = 4.dp
): Modifier = composed {
    return@composed this.then(
        padding(start = start, top = top, end = end, bottom = bottom)
    )
}

@Composable
fun Modifier.epgBorder(
    isHighlight: Boolean = false,
    roundedCorner: Dp = 4.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
): Modifier = composed {
    return@composed this.border(
        width = if (isHighlight) 2.dp else (-1).dp,
        color = borderColor,
        shape = RoundedCornerShape(roundedCorner)
    )
}

fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}
