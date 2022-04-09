package cafe.adriel.bonsai.core.component

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
import cafe.adriel.bonsai.core.Node
import cafe.adriel.bonsai.core.Tree

public typealias ToggleNode = Boolean
public typealias OnNodeClick<T> = (Node<T>) -> ToggleNode

internal data class TreeScope<T>(
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
    public val innerLevelPadding: PaddingValues = PaddingValues(start = nodeIconSize),
)

@Composable
public fun <T> Tree(
    tree: Tree<T>,
    onClick: OnNodeClick<T>? = null,
    onLongClick: OnNodeClick<T>? = null,
    onDoubleClick: OnNodeClick<T>? = null,
    modifier: Modifier = Modifier,
    style: TreeStyle = TreeStyle()
) {
    with(
        TreeScope(
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
            items(tree.nodes) { node -> Node(node) }
        }
    }
}
