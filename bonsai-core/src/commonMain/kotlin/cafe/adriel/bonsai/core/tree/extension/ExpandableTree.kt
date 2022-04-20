package cafe.adriel.bonsai.core.tree.extension

import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Node

public interface ExpandableTree<T> {

    public fun toggleExpansion(node: Node<T>)

    public fun collapseRoot()

    public fun expandRoot()

    public fun collapseAll()

    public fun expandAll()

    public fun collapseFrom(depth: Int)

    public fun expandUntil(depth: Int)

    public fun collapseNode(node: Node<T>)

    public fun expandNode(node: Node<T>)
}

internal class ExpandableTreeHandler<T>(
    private val nodes: List<Node<T>>
) : ExpandableTree<T> {

    override fun toggleExpansion(node: Node<T>) {
        if (node !is BranchNode) return

        if (node.isExpanded) collapseNode(node)
        else expandNode(node)
    }

    override fun collapseRoot() {
        collapse(nodes, depth = 0)
    }

    override fun expandRoot() {
        expand(nodes, depth = 0)
    }

    override fun collapseAll() {
        collapse(nodes, depth = 0)
    }

    override fun expandAll() {
        expand(nodes, depth = Int.MAX_VALUE)
    }

    override fun collapseFrom(depth: Int) {
        collapse(nodes, depth)
    }

    override fun expandUntil(depth: Int) {
        expand(nodes, depth)
    }

    override fun collapseNode(node: Node<T>) {
        collapse(listOf(node), node.depth)
    }

    override fun expandNode(node: Node<T>) {
        expand(listOf(node), node.depth)
    }

    private fun collapse(nodes: List<Node<T>>, depth: Int) {
        nodes.asSequence()
            .filterIsInstance<BranchNode<T>>()
            .filter { it.depth >= depth }
            .sortedByDescending { it.depth }
            .forEach { it.setExpanded(false, depth) }
    }

    private fun expand(nodes: List<Node<T>>, depth: Int) {
        nodes.asSequence()
            .filterIsInstance<BranchNode<T>>()
            .filter { it.depth <= depth }
            .sortedBy { it.depth }
            .forEach { it.setExpanded(true, depth) }
    }
}
