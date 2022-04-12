package cafe.adriel.bonsai.core.node

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import cafe.adriel.bonsai.core.BonsaiScope

public interface BranchNode<T> : Node<T> {

    public var isExpanded: Boolean

    public val children: MutableList<Node<T>>

    @Composable
    override fun BonsaiScope<T>.NodeIcon() {
        val icon = if (isExpanded) {
            style.nodeExpandedIcon(this@BranchNode)
        } else {
            style.nodeCollapsedIcon(this@BranchNode)
        }

        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = name
            )
        }
    }

    @Composable
    override fun BonsaiScope<T>.NodeName() {
        BasicText(
            text = name,
            style = style.nodeNameTextStyle,
            modifier = Modifier.padding(start = style.nodeNameStartPadding)
        )
    }
}

public fun <T> BranchNode(
    content: T,
    name: String = content.toString(),
    level: Int = 0,
    parent: Node<T>? = null,
    isSelected: Boolean = false,
    isExpanded: Boolean = false,
    children: MutableList<Node<T>> = mutableListOf()
): BranchNode<T> =
    object : BranchNode<T> {
        override val content: T = content
        override val name: String = name
        override val level: Int = level
        override val parent: Node<T>? = parent
        override var isSelected: Boolean by mutableStateOf(isSelected)
        override var isExpanded: Boolean by mutableStateOf(isExpanded)
        override val children: MutableList<Node<T>> = children.toMutableStateList()
    }
