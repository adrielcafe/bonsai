package cafe.adriel.bonsai.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
    val (isExpanded, setExpanded) = rememberSaveable {
        mutableStateOf(
            if (node is BranchNode) node.startExpanded
            else false
        )
    }
    val state = remember(level, isExpanded) { NodeState(level, isExpanded) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleIcon(node, state, isExpanded, setExpanded)
        NodeContent(node, state, isExpanded, setExpanded)
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
    setExpanded: (Boolean) -> Unit,
) {
    if (style.toggleIcon == null) return

    if (node is BranchNode) {
        val rotation by animateFloatAsState(
            if (isExpanded && style.enableToggleIconRotation) 90f
            else 0f
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(style.toggleShape)
                .then(clickableNode(node, state, isExpanded, setExpanded, true))
                .size(style.nodeIconSize)
                .requiredSize(style.toggleIconSize)
                .rotate(rotation)
        ) {
            Image(
                painter = style.toggleIcon,
                contentDescription = "Toggle"
            )
        }
    } else {
        Spacer(Modifier.size(style.nodeIconSize))
    }
}

@Composable
private fun <T> TreeScope<T>.NodeContent(
    node: Node<T>,
    state: NodeState,
    isExpanded: Boolean,
    setExpanded: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(style.nodeShape)
            .then(clickableNode(node, state, isExpanded, setExpanded))
            .padding(style.nodePadding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.requiredSize(style.nodeIconSize)
        ) {
            node.NodeIcon(state)
        }
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
            modifier = Modifier.padding(style.indentationPadding)
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
    isExpanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    forceSingleClick: Boolean = false
): Modifier {
    val toggle = { setExpanded(isExpanded.not()) }

    return if (forceSingleClick || onLongClick == null && onDoubleClick == null) {
        Modifier.clickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node, state) == true) toggle() }
        )
    } else {
        Modifier.combinedClickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node, state) == true) toggle() },
            onDoubleClick = { if (onDoubleClick?.invoke(node, state) == true) toggle() },
            onLongClick = { if (onLongClick?.invoke(node, state) == true) toggle() }
        )
    }
}
