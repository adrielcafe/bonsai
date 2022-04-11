package cafe.adriel.bonsai.core.tree.selectable

import cafe.adriel.bonsai.core.node.Node

public interface SelectableTree<T> {

    public val selectedNodes: MutableList<Node<T>>

    public fun toggleSelection(node: Node<T>)

    public fun selectNode(node: Node<T>)

    public fun unselectNode(node: Node<T>)

    public fun clearSelection()
}
