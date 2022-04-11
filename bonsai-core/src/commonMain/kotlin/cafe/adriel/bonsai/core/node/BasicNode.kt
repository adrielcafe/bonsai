package cafe.adriel.bonsai.core.node

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    override fun NodeIcon() {
        BasicNodeIcon()
    }

    @Composable
    override fun NodeName() {
        BasicNodeName()
    }
}

public open class BasicLeafNode<T>(
    override val content: T,
    override val name: String = content.toString(),
    override val level: Int,
    override val parent: Node<T>?,
    override val style: BasicNodeStyle = BasicNodeStyle()
) : LeafNode<T>, BasicNode<T>

public open class BasicBranchNode<T>(
    override val content: T,
    override val name: String = content.toString(),
    override val level: Int,
    override val parent: Node<T>?,
    override val style: BasicNodeStyle = BasicNodeStyle(),
    override val children: SnapshotStateList<Node<T>>,
    override var isExpanded: MutableState<Boolean> = mutableStateOf(false),
) : BranchNode<T>, BasicNode<T>

public data class BasicNodeStyle(
    public val collapsedIcon: @Composable () -> Painter? = { null },
    public val expandedIcon: @Composable () -> Painter? = { null },
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
private fun <T> BasicNode<T>.BasicNodeIcon() {
    val icon = if (this is BasicBranchNode && isExpanded.value) {
        style.expandedIcon()
    } else {
        style.collapsedIcon()
    }

    if (icon != null) {
        Image(
            painter = icon,
            contentDescription = name
        )
    }
}

@Composable
private fun <T> BasicNode<T>.BasicNodeName() {
    BasicText(
        text = name,
        style = style.textStyle,
        modifier = Modifier.padding(start = style.nodeNameStartPadding)
    )
}
