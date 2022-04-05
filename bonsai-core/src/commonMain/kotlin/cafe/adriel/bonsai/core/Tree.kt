package cafe.adriel.bonsai.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public typealias ToggleNode = Boolean
public typealias OnNodeClick<T> = (Node<T>, NodeState) -> ToggleNode

internal data class TreeScope<T>(
    val level: Int,
    val style: TreeStyle,
    val onClick: OnNodeClick<T>?,
    val onLongClick: OnNodeClick<T>?,
    val onDoubleClick: OnNodeClick<T>?,
)

public data class TreeStyle(
    public val expandTransition: EnterTransition = fadeIn() + expandVertically(),
    public val collapseTransition: ExitTransition = fadeOut() + shrinkVertically(),
    public val toggleIcon: Painter? = null,
    public val toggleIconSize: Dp = 16.dp,
    public val toggleShape: Shape = CircleShape,
    public val enableToggleIconRotation: Boolean = true,
    public val nodeIconSize: Dp = 24.dp,
    public val nodePadding: PaddingValues = PaddingValues(all = 4.dp),
    public val nodeShape: Shape = RoundedCornerShape(size = 4.dp),
    public val indentationPadding: PaddingValues = PaddingValues(start = nodeIconSize),
)

@Composable
public fun <T> Tree(
    nodes: List<Node<T>>,
    onClick: OnNodeClick<T>,
    onLongClick: OnNodeClick<T>? = null,
    onDoubleClick: OnNodeClick<T>? = null,
    modifier: Modifier = Modifier,
    style: TreeStyle = TreeStyle()
) {
    with(
        TreeScope(
            level = 0,
            style = style,
            onClick = onClick,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
        )
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            items(nodes) { node -> Node(node) }
        }
    }
}
