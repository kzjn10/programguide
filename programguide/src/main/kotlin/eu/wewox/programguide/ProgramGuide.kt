package eu.wewox.programguide

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import eu.wewox.minabox.MinaBox
import eu.wewox.minabox.MinaBoxItem
import eu.wewox.minabox.MinaBoxScope
import eu.wewox.minabox.MinaBoxScrollDirection
import eu.wewox.minabox.MinaBoxState
import java.lang.System.currentTimeMillis
import kotlin.math.abs

/**
 * Lazy layout to display program guide data on the two directional plane.
 * Items should be provided with [content] lambda.
 *
 * @param modifier The modifier instance for the root composable.
 * @param state The state which could be used to observe and change translation offset.
 * @param dimensions The dimensions of the program guide.
 * @param contentPadding A padding around the whole content. This will add padding for the content
 * after it has been clipped, which is not possible via modifier param.
 * @param content The lambda block which describes the content. Inside this block you can use
 * [ProgramGuideScope.programs] / [ProgramGuideScope.channels] methods to add items.
 */
@Composable
public fun ProgramGuide(
    modifier: Modifier = Modifier,
    state: ProgramGuideState = rememberProgramGuideState(),
    dimensions: ProgramGuideDimensions = ProgramGuideDefaults.dimensions,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ProgramGuideScope.() -> Unit
) {
    val dimensionsPx = dimensions.roundToPx(LocalDensity.current)
    val scope = ProgramGuideScopeImpl().apply(content)
    var lastTranslate: MinaBoxState.Translate? = null
    var scrollingDirection by remember { mutableStateOf(MinaBoxScrollDirection.BOTH) }

    state.dimensions = dimensionsPx
    state.indexMapper = ProgramGuideIndexMapper(
        programsCount = scope.programsContent?.count ?: 0,
        currentTimeCount = scope.currentTimeContent?.count ?: 0,
        channelsCount = scope.channelsContent?.count ?: 0,
    )
    LaunchedEffect(state) {
        snapshotFlow { state.minaBoxState.translate }
            .collect { translate ->
                translate?.let { current ->
                    lastTranslate?.let { last ->
                        val offsetX = abs(current.x - last.x)
                        val offsetY = abs(current.y - last.y)
                        if (scrollingDirection == MinaBoxScrollDirection.BOTH) {
                            if (offsetX > offsetY && offsetX - offsetY > 5.0) {
                                scrollingDirection = MinaBoxScrollDirection.HORIZONTAL
                            } else if (offsetX < offsetY && offsetY - offsetX > 5.0) {
                                scrollingDirection = MinaBoxScrollDirection.VERTICAL
                            }
                        } else if (offsetX < 1.0 && offsetY < 1.0) {
                            scrollingDirection = MinaBoxScrollDirection.BOTH
                        }
                        lastTranslate = current
                    } ?: run {
                        lastTranslate = current
                    }
                }
            }
    }

    MinaBox(
        state = state.minaBoxState,
        contentPadding = contentPadding,
        modifier = modifier,
        scrollDirection = scrollingDirection,
    ) {
        items(
            toMinaBoxItem = { toMinaBoxItem(scope.guideStartHour, dimensionsPx) },
            scope.programsContent,
            scope.currentTimeContent,
            scope.channelsContent,
            scope.timelinesContent,
            scope.topCornerContent
        )
    }
}

private fun MinaBoxScope.items(
    toMinaBoxItem: ProgramGuideItem.() -> MinaBoxItem,
    vararg items: ProgramGuideItemContent?,
) {
    if (items.isEmpty()) {
        return
    }
    items.filterNotNull().forEach { item ->
        items(
            count = item.count,
            layoutInfo = { item.layoutInfo(it).toMinaBoxItem() },
            key = item.key,
            contentType = item.contentType,
            itemContent = item.itemContent,
        )
    }
}

/**
 * Contains the default values for [ProgramGuide].
 */
public object ProgramGuideDefaults {

    /**
     * The default dimensions of the program guide.
     */
    public val dimensions: ProgramGuideDimensions =
        ProgramGuideDimensions(
            timelineHourWidth = 128.dp,
            timelineHeight = 32.dp,
            channelWidth = 64.dp,
            channelHeight = 64.dp,
            currentTimeWidth = 2.dp,
        )
}
