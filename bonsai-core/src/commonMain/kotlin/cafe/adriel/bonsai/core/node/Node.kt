package cafe.adriel.bonsai.core.node

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import cafe.adriel.bonsai.core.BonsaiScope
import cafe.adriel.bonsai.core.util.Saveable

public interface Node<T> : Saveable {

    public val content: T

    public val level: Int

    public val parent: Node<T>?

    @Composable
    public fun NodeIcon()

    @Composable
    public fun NodeName()
}

public interface BranchNode<T> : Node<T> {

    public var isExpanded: MutableState<Boolean>

    public val children: SnapshotStateList<Node<T>>
}

public interface LeafNode<T> : Node<T>

@Composable
internal fun <T> BonsaiScope<T>.Node(
    node: Node<T>
) {
    val toggleExpansion = {
        if (node is BranchNode) {
            node.isExpanded.value = node.isExpanded.value.not()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleIcon(node, toggleExpansion)
        NodeContent(node, toggleExpansion)
    }

    if (node is BranchNode) {
        AnimatedVisibility(
            visible = node.isExpanded.value,
            enter = style.expandTransition,
            exit = style.collapseTransition
        ) {
            ExpandedNode(node.children)
        }
    }
}

@Composable
private fun <T> BonsaiScope<T>.ToggleIcon(
    node: Node<T>,
    toggleExpansion: () -> Unit,
) {
    if (style.toggleIcon == null) return

    if (node is BranchNode) {
        val rotationDegrees by animateFloatAsState(
            if (node.isExpanded.value && style.enableToggleIconRotation) 90f else 0f
        )

        Image(
            painter = style.toggleIcon,
            contentDescription = "Toggle",
            modifier = Modifier
                .clip(style.toggleShape)
                .then(clickableNode(node, toggleExpansion, forceSingleClick = true))
                .size(style.nodeIconSize)
                .requiredSize(style.toggleIconSize)
                .rotate(rotationDegrees)
        )
    } else {
        Spacer(Modifier.size(style.nodeIconSize))
    }
}

@Composable
private fun <T> BonsaiScope<T>.NodeContent(
    node: Node<T>,
    toggleExpansion: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(style.nodeShape)
            .then(clickableNode(node, toggleExpansion))
            .padding(style.nodePadding)
            .requiredHeight(style.nodeIconSize)
    ) {
        node.NodeIcon()
        node.NodeName()
    }
}

@Composable
private fun <T> BonsaiScope<T>.ExpandedNode(
    nodes: SnapshotStateList<Node<T>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(style.innerLevelPadding)
    ) {
        nodes.forEach { node -> Node(node) }
    }
}

// TODO use context receiver (aka coeffects) when stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> BonsaiScope<T>.clickableNode(
    node: Node<T>,
    toggleExpansion: () -> Unit,
    forceSingleClick: Boolean = false
): Modifier =
    if (forceSingleClick || onLongClick == null && onDoubleClick == null) {
        Modifier.clickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node) == true) toggleExpansion() }
        )
    } else {
        Modifier.combinedClickable(
            role = Role.Button,
            onClick = { if (onClick?.invoke(node) == true) toggleExpansion() },
            onDoubleClick = { if (onDoubleClick?.invoke(node) == true) toggleExpansion() },
            onLongClick = { if (onLongClick?.invoke(node) == true) toggleExpansion() }
        )
    }
