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
                contentDescription = name,
                colorFilter = style.nodeCollapsedColorFilter,
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

public class SimpleLeafNode<T>(
    override val content: T,
    override val name: String = content.toString(),
    override val parent: Node<T>? = null,
    isSelected: Boolean = false
) : LeafNode<T> {
    override val level: Int = parent?.level?.inc() ?: 0
    override var isSelected: Boolean by mutableStateOf(isSelected)
}
