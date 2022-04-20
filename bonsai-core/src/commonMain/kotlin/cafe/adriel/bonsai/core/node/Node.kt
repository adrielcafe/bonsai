package cafe.adriel.bonsai.core.node

import androidx.compose.runtime.Composable
import cafe.adriel.bonsai.core.BonsaiScope
import cafe.adriel.bonsai.core.node.extension.ExpandableNode
import cafe.adriel.bonsai.core.node.extension.ExpandableNodeHandler
import cafe.adriel.bonsai.core.node.extension.SelectableNode
import cafe.adriel.bonsai.core.node.extension.SelectableNodeHandler
import cafe.adriel.bonsai.core.util.randomUUID

public typealias NodeComponent<T> = @Composable BonsaiScope<T>.(Node<T>) -> Unit

public sealed interface Node<T> {

    public val key: String

    public val content: T

    public val name: String

    public val depth: Int

    public val isSelected: Boolean

    public val iconComponent: NodeComponent<T>

    public val nameComponent: NodeComponent<T>
}

public class LeafNode<T> internal constructor(
    override val content: T,
    override val depth: Int,
    override val key: String = randomUUID,
    override val name: String = content.toString(),
    override val iconComponent: NodeComponent<T> = { DefaultNodeIcon(it) },
    override val nameComponent: NodeComponent<T> = { DefaultNodeName(it) }
) : Node<T>,
    SelectableNode by SelectableNodeHandler()

public class BranchNode<T> internal constructor(
    override val content: T,
    override val depth: Int,
    override val key: String = randomUUID,
    override val name: String = content.toString(),
    override val iconComponent: NodeComponent<T> = { DefaultNodeIcon(it) },
    override val nameComponent: NodeComponent<T> = { DefaultNodeName(it) }
) : Node<T>,
    SelectableNode by SelectableNodeHandler(),
    ExpandableNode by ExpandableNodeHandler()
