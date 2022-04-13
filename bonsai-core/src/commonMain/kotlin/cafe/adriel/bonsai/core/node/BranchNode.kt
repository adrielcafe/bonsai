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
        val (icon, colorFilter) = if (isExpanded) {
            style.nodeExpandedIcon(this@BranchNode) to style.nodeExpandedColorFilter
        } else {
            style.nodeCollapsedIcon(this@BranchNode) to style.nodeCollapsedColorFilter
        }

        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = name,
                colorFilter = colorFilter,
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

public class SimpleBranchNode<T>(
    override val content: T,
    children: (BranchNode<T>) -> List<Node<T>>,
    override val name: String = content.toString(),
    override val parent: Node<T>? = null,
    isSelected: Boolean = false,
    isExpanded: Boolean = false,
) : BranchNode<T> {
    override val level: Int = parent?.level?.inc() ?: 0
    override var isSelected: Boolean by mutableStateOf(isSelected)
    override var isExpanded: Boolean by mutableStateOf(isExpanded)
    override val children: MutableList<Node<T>> by lazy { children(this).toMutableStateList() }
}
