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

public open class BasicLeafNode<T>(
    override val content: T,
    public val name: String = content.toString(),
    public val style: BasicNodeStyle = BasicNodeStyle()
) : LeafNode<T> {

    @Composable
    override fun NodeIcon(state: NodeState) {
        BasicNodeIcon(name, state, style)
    }

    @Composable
    override fun NodeName(state: NodeState) {
        BasicNodeName(name, style)
    }
}

public open class BasicBranchNode<T>(
    override val content: T,
    public val name: String = content.toString(),
    public val style: BasicNodeStyle = BasicNodeStyle(),
    override val startExpanded: Boolean = false,
    override val children: ChildrenNodes<T>,
) : BranchNode<T> {

    @Composable
    override fun NodeIcon(state: NodeState) {
        BasicNodeIcon(name, state, style)
    }

    @Composable
    override fun NodeName(state: NodeState) {
        BasicNodeName(name, style)
    }
}

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
    val icon = if (state.isExpanded) {
        style.expandedIcon ?: style.collapsedIcon
    } else {
        style.collapsedIcon ?: style.expandedIcon
    }

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
