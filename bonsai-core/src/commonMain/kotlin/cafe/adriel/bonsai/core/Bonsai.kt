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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.core.tree.expandable.ExpandableTree
import cafe.adriel.bonsai.core.tree.selectable.SelectableTree

public typealias OnNodeClick<T> = ((Node<T>) -> Unit)?
public typealias NodeIcon<T> = @Composable (Node<T>) -> Painter?

public data class BonsaiScope<T>(
    val expandableManager: ExpandableTree<T>,
    val selectableManager: SelectableTree<T>,
    val style: BonsaiStyle<T>,
    val onClick: OnNodeClick<T>,
    val onLongClick: OnNodeClick<T>,
    val onDoubleClick: OnNodeClick<T>,
)

public data class BonsaiStyle<T>(
    public val expandTransition: EnterTransition = fadeIn() + expandVertically(),
    public val collapseTransition: ExitTransition = fadeOut() + shrinkVertically(),
    public val toggleIcon: NodeIcon<T> = { rememberVectorPainter(Icons.Default.ChevronRight) },
    public val toggleIconSize: Dp = 16.dp,
    public val toggleIconColorFilter: ColorFilter? = null,
    public val toggleShape: Shape = CircleShape,
    public val toggleIconRotationDegrees: Float = 90f,
    public val nodeIconSize: Dp = 24.dp,
    public val nodePadding: PaddingValues = PaddingValues(all = 4.dp),
    public val nodeShape: Shape = RoundedCornerShape(size = 4.dp),
    public val nodeSelectedBackgroundColor: Color = Color.LightGray.copy(alpha = .8f),
    public val nodeCollapsedIcon: NodeIcon<T> = { null },
    public val nodeCollapsedColorFilter: ColorFilter? = null,
    public val nodeExpandedIcon: NodeIcon<T> = nodeCollapsedIcon,
    public val nodeExpandedColorFilter: ColorFilter? = nodeCollapsedColorFilter,
    public val nodeNameStartPadding: Dp = 0.dp,
    public val nodeNameTextStyle: TextStyle = DefaultNodeTextStyle
) {

    public companion object {
        public val DefaultNodeTextStyle: TextStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

@Composable
public fun <T> Bonsai(
    tree: Tree<T>,
    modifier: Modifier = Modifier,
    onClick: OnNodeClick<T> = tree::onNodeClick,
    onDoubleClick: OnNodeClick<T> = tree::onNodeClick,
    onLongClick: OnNodeClick<T> = tree::toggleSelection,
    style: BonsaiStyle<T> = BonsaiStyle(),
) {
    with(
        BonsaiScope(
            expandableManager = tree,
            selectableManager = tree,
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

private fun <T> Tree<T>.onNodeClick(node: Node<T>) {
    clearSelection()
    toggleExpansion(node)
}
