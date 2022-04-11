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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.tree.Tree

public typealias OnNodeClick<T> = ((Node<T>) -> Unit)?

internal data class BonsaiScope<T>(
    val style: BonsaiStyle,
    val onClick: OnNodeClick<T>,
    val onLongClick: OnNodeClick<T>,
    val onDoubleClick: OnNodeClick<T>,
)

public data class BonsaiStyle(
    public val expandTransition: EnterTransition = fadeIn() + expandVertically(),
    public val collapseTransition: ExitTransition = fadeOut() + shrinkVertically(),
    public val toggleIcon: Painter? = null,
    public val toggleIconSize: Dp = 16.dp,
    public val toggleShape: Shape = CircleShape,
    public val enableToggleIconRotation: Boolean = true,
    public val nodeIconSize: Dp = 24.dp,
    public val nodePadding: PaddingValues = PaddingValues(all = 4.dp),
    public val nodeShape: Shape = RoundedCornerShape(size = 4.dp),
    public val nodeSelectedBackgroundColor: Color = Color.LightGray.copy(alpha = .7f)
)

@Composable
public fun <T> Bonsai(
    tree: Tree<T>,
    modifier: Modifier = Modifier,
    onClick: OnNodeClick<T> = tree::toggleExpansion,
    onLongClick: OnNodeClick<T> = tree::toggleSelection,
    onDoubleClick: OnNodeClick<T> = tree::toggleExpansion,
    style: BonsaiStyle = BonsaiStyle(),
) {
    with(
        BonsaiScope(
            onClick = onClick,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
            style = style,
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
