package cafe.adriel.bonsai.core.tree

import androidx.compose.runtime.Composable
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.node.SimpleBranchNode
import cafe.adriel.bonsai.core.node.SimpleLeafNode

@DslMarker
private annotation class TreeMarker

@TreeMarker
public class NodeBuilder<T> internal constructor(
    private val parent: Node<T>? = null
) {

    internal val nodes = mutableListOf<Node<T>>()

    public fun Leaf(
        content: T,
        name: String = content.toString()
    ) {
        nodes += SimpleLeafNode(
            content = content,
            name = name,
            parent = parent
        )
    }

    public fun Branch(
        content: T,
        name: String = content.toString(),
        children: NodeBuilder<T>.() -> Unit = {}
    ) {
        nodes += SimpleBranchNode(
            content = content,
            name = name,
            parent = parent,
            children = { node ->
                NodeBuilder(parent = node).run {
                    children()
                    nodes
                }
            }
        )
    }
}

@Composable
public fun <T> Tree(
    init: NodeBuilder<T>.() -> Unit
): Tree<T> =
    NodeBuilder<T>().run {
        init()
        rememberTree(nodes)
    }
