package cafe.adriel.bonsai.core.tree.extension

import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.LeafNode
import cafe.adriel.bonsai.core.node.Node

public interface SelectableTree<T> {

    public val selectedNodes: List<Node<T>>

    public fun toggleSelection(node: Node<T>)

    public fun selectNode(node: Node<T>)

    public fun unselectNode(node: Node<T>)

    public fun clearSelection()
}

internal class SelectableTreeHandler<T>(
    private val nodes: List<Node<T>>
) : SelectableTree<T> {

    override val selectedNodes: List<Node<T>>
        get() = nodes.filter { it.isSelected }

    override fun toggleSelection(node: Node<T>) {
        if (node.isSelected) unselectNode(node)
        else selectNode(node)
    }

    override fun selectNode(node: Node<T>) {
        node.setSelected(true)
    }

    override fun unselectNode(node: Node<T>) {
        node.setSelected(false)
    }

    override fun clearSelection() {
        selectedNodes.forEach { it.setSelected(false) }
    }

    private fun Node<T>.setSelected(isSelected: Boolean) {
        when (this) {
            is LeafNode -> setSelected(isSelected)
            is BranchNode -> setSelected(isSelected)
        }
    }
}
