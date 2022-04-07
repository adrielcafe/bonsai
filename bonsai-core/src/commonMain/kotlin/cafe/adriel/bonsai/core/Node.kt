package cafe.adriel.bonsai.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role

public typealias ChildrenNodes<T> = @Composable (T) -> List<Node<T>>

public interface Node<T> {

    public val content: T

    @Composable
    public fun NodeIcon(state: NodeState)

    @Composable
    public fun NodeName(state: NodeState)
}

public interface LeafNode<T> : Node<T>

public interface BranchNode<T> : Node<T> {

    public val startExpanded: Boolean

    public val children: ChildrenNodes<T>
}

public data class NodeState(
    public val level: Int,
    public val isExpanded: Boolean
)

@Composable
internal fun <T> TreeScope<T>.Node(
    node: Node<T>
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(node is BranchNode && node.startExpanded)
    }
    val state = remember(level, isExpanded) {
        NodeState(level, isExpanded)
    }
    val toggleExpansion = { isExpanded = isExpanded.not() }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleIcon(node, state, isExpanded, toggleExpansion)
        NodeContent(node, state, toggleExpansion)
    }

    if (node is BranchNode) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = style.expandTransition,
            exit = style.collapseTransition
        ) {
            ExpandedNode(node.children(node.content))
        }
    }
}

@Composable
private fun <T> TreeScope<T>.ToggleIcon(
    node: Node<T>,
    state: NodeState,
    isExpanded: Boolean,
    toggleExpansion: () -> Unit,
) {
    if (style.toggleIcon == null) return

    if (node is BranchNode) {
        val rotationDegrees by animateFloatAsState(
            if (isExpanded && style.enableToggleIconRotation) 90f else 0f
        )

        Image(
            painter = style.toggleIcon,
            contentDescription = "Toggle",
            modifier = Modifier
                .clip(style.toggleShape)
                .then(clickableNode(node, state, toggleExpansion, forceSingleClick = true))
                .size(style.nodeIconSize)
                .requiredSize(style.toggleIconSize)
                .rotate(rotationDegrees)
        )
    } else {
        Spacer(Modifier.size(style.nodeIconSize))
    }
}

@Composable
private fun <T> TreeScope<T>.NodeContent(
    node: Node<T>,
    state: NodeState,
    toggleExpansion: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(style.nodeShape)
            .then(clickableNode(node, state, toggleExpansion))
            .padding(style.nodePadding)
            .requiredHeight(style.nodeIconSize)
    ) {
        node.NodeIcon(state)
        node.NodeName(state)
    }
}

@Composable
private fun <T> TreeScope<T>.ExpandedNode(
    nodes: List<Node<T>>
) {
    val nodeScope = copy(level = level.inc())

    with(nodeScope) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(style.innerLevelPadding)
        ) {
            nodes.forEach { node -> Node(node) }
        }
    }
}

// TODO use context receiver (aka coeffects) when stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> TreeScope<T>.clickableNode(
    node: Node<T>,
    state: NodeState,
    toggleExpansion: () -> Unit,
    forceSingleClick: Boolean = false
): Modifier =
    if (forceSingleClick || onLongClick == null && onDoubleClick == null) {
        Modifier.clickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node, state) == true) toggleExpansion() }
        )
    } else {
        Modifier.combinedClickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node, state) == true) toggleExpansion() },
            onDoubleClick = { if (onDoubleClick?.invoke(node, state) == true) toggleExpansion() },
            onLongClick = { if (onLongClick?.invoke(node, state) == true) toggleExpansion() }
        )
    }
