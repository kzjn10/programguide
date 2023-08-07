package eu.wewox.programguide.demo.ui.components.epg.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eu.wewox.programguide.demo.extensions.epgBackground
import eu.wewox.programguide.demo.extensions.epgPadding


@Composable
fun EPGTopCorner(
    modifier: Modifier = Modifier,
    date: String = "Today",
    indicationInvisible: Boolean = false,
) {
    Surface(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
                .epgPadding()
                .epgBackground()

        ) {
            val (indicator, text) = createRefs()

            val textModifier = Modifier.constrainAs(text) {
                linkTo(
                    top = parent.top,
                    bottom = parent.bottom,
                    start = parent.start,
                    end = parent.end
                )
            }

            val indicatorModifier = Modifier
                .constrainAs(indicator) {
                    linkTo(text.top, text.bottom)
                    linkTo(text.start, text.start, startMargin = (-12).dp)
                    width = Dimension.preferredValue(4.dp)
                    height = Dimension.fillToConstraints
                }
                .background(MaterialTheme.colorScheme.primary)

            Text(text = date, modifier = textModifier)

            AnimatedVisibility(
                visible = indicationInvisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = indicatorModifier
            ) {
                Spacer(modifier = Modifier)
            }

        }
    }
}
