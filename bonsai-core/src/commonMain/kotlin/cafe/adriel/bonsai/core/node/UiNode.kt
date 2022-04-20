package cafe.adriel.bonsai.core.node

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import cafe.adriel.bonsai.core.BonsaiScope

@Composable
internal fun <T> BonsaiScope<T>.Node(
    node: Node<T>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 1.dp)
            .padding(start = node.depth * style.toggleIconSize)
    ) {
        ToggleIcon(node)
        NodeContent(node)
    }
}

@Composable
private fun <T> BonsaiScope<T>.ToggleIcon(
    node: Node<T>
) {
    val toggleIcon = style.toggleIcon(node) ?: return

    if (node is BranchNode) {
        val rotationDegrees by animateFloatAsState(
            if (node.isExpanded) style.toggleIconRotationDegrees else 0f
        )

        Image(
            painter = toggleIcon,
            contentDescription = if (node.isExpanded) "Collapse node" else "Expand node",
            colorFilter = style.toggleIconColorFilter,
            modifier = Modifier
                .clip(style.toggleShape)
                .clickable { expandableManager.toggleExpansion(node) }
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
    node: Node<T>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .run {
                if (node.isSelected.not()) clip(style.nodeShape)
                else background(style.nodeSelectedBackgroundColor, style.nodeShape)
            }
            .then(clickableNode(node))
            .padding(style.nodePadding)
            .requiredHeight(style.nodeIconSize)
    ) {
        with(node) {
            iconComponent(node)
            nameComponent(node)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> BonsaiScope<T>.clickableNode(
    node: Node<T>
): Modifier =
    if (onLongClick == null && onDoubleClick == null) {
        Modifier.clickable { onClick?.invoke(node) }
    } else {
        Modifier.combinedClickable(
            onClick = { onClick?.invoke(node) },
            onDoubleClick = { onDoubleClick?.invoke(node) },
            onLongClick = { onLongClick?.invoke(node) }
        )
    }

@Composable
internal fun <T> BonsaiScope<T>.DefaultNodeIcon(node: Node<T>) {
    val (icon, colorFilter) = if (node is BranchNode && node.isExpanded) {
        style.nodeExpandedIcon(node) to style.nodeExpandedIconColorFilter
    } else {
        style.nodeCollapsedIcon(node) to style.nodeCollapsedIconColorFilter
    }

    if (icon != null) {
        Image(
            painter = icon,
            colorFilter = colorFilter,
            contentDescription = node.name,
        )
    }
}

@Composable
internal fun <T> BonsaiScope<T>.DefaultNodeName(node: Node<T>) {
    BasicText(
        text = node.name,
        style = style.nodeNameTextStyle,
        modifier = Modifier.padding(start = style.nodeNameStartPadding)
    )
}
