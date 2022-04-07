package cafe.adriel.bonsai.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public interface BasicNode<T> : Node<T> {

    public val name: String

    public val style: BasicNodeStyle

    @Composable
    override fun NodeIcon(state: NodeState) {
        BasicNodeIcon(name, state, style)
    }

    @Composable
    override fun NodeName(state: NodeState) {
        BasicNodeName(name, style)
    }
}

public open class BasicLeafNode<T>(
    override val content: T,
    override val name: String = content.toString(),
    override val style: BasicNodeStyle = BasicNodeStyle()
) : LeafNode<T>, BasicNode<T>

public open class BasicBranchNode<T>(
    override val content: T,
    override val name: String = content.toString(),
    override val style: BasicNodeStyle = BasicNodeStyle(),
    override val startExpanded: Boolean = false,
    override val children: ChildrenNodes<T>,
) : BranchNode<T>, BasicNode<T>

public data class BasicNodeStyle(
    public val collapsedIcon: Painter? = null,
    public val expandedIcon: Painter? = collapsedIcon,
    public val nodeNameStartPadding: Dp = 4.dp,
    public val textStyle: TextStyle = DefaultTextStyle
) {

    public companion object {
        public val DefaultTextStyle: TextStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp
        )
    }
}

@Composable
private fun BasicNodeIcon(
    name: String,
    state: NodeState,
    style: BasicNodeStyle
) {
    val icon = if (state.isExpanded) style.expandedIcon else style.collapsedIcon

    if (icon != null) {
        Image(
            painter = icon,
            contentDescription = name
        )
    }
}

@Composable
private fun BasicNodeName(
    name: String,
    style: BasicNodeStyle
) {
    BasicText(
        text = name,
        style = style.textStyle,
        modifier = Modifier.padding(start = style.nodeNameStartPadding)
    )
}
