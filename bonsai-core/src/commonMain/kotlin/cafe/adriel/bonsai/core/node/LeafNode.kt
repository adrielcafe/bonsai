package cafe.adriel.bonsai.core.node

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.bonsai.core.BonsaiScope

public interface LeafNode<T> : Node<T> {

    @Composable
    override fun BonsaiScope<T>.NodeIcon() {
        val icon = style.nodeCollapsedIcon(this@LeafNode)

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

public fun <T> LeafNode(
    content: T,
    name: String = content.toString(),
    level: Int = 0,
    parent: Node<T>? = null,
    isSelected: Boolean = false
): LeafNode<T> =
    object : LeafNode<T> {
        override val content: T = content
        override val name: String = name
        override val level: Int = level
        override val parent: Node<T>? = parent
        override var isSelected: Boolean by mutableStateOf(isSelected)
    }
