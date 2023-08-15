package eu.wewox.programguide.demo.ui.components.epg.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun EPGCurrentTimeLine(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier, color = Color.Transparent) {
        ConstraintLayout(modifier = Modifier) {
            val (text, line) = createRefs()

            val textModifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)


            val lineModifier = Modifier
                .constrainAs(line) {
                    top.linkTo(text.top)
                    start.linkTo(text.start)
                    end.linkTo(text.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.preferredValue(2.dp)
                }
                .background(MaterialTheme.colorScheme.error)

            Box(modifier = lineModifier)

            if (label.isNotBlank())
                Text(
                    modifier = textModifier,
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                )

        }
    }
}