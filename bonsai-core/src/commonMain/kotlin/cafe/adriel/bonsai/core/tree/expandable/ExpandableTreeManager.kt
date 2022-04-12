package cafe.adriel.bonsai.core.tree.expandable

import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Node

public class ExpandableTreeManager<T>(
    private val nodes: List<Node<T>>
) : ExpandableTree<T> {

    override fun toggleExpansion(node: Node<T>) {
        if (node is BranchNode && node.isExpanded) collapseNode(node)
        else expandNode(node)
    }

    override fun collapseAll() {
        collapseDown(nodes, minLevel = Int.MAX_VALUE)
    }

    override fun expandAll() {
        expandDown(nodes, maxLevel = Int.MAX_VALUE)
    }

    override fun collapseRoot() {
        collapseDown(nodes, minLevel = 0)
    }

    override fun expandRoot() {
        expandDown(nodes, maxLevel = 0)
    }

    override fun collapseFrom(minLevel: Int) {
        collapseDown(nodes, minLevel)
    }

    override fun expandUntil(maxLevel: Int) {
        expandDown(nodes, maxLevel)
    }

    override fun collapseNode(node: Node<T>) {
        collapseDown(listOf(node), minLevel = 0)
    }

    override fun expandNode(node: Node<T>, maxLevel: Int) {
        expandUp(node.parent)
        expandDown(listOf(node), maxLevel = maxLevel)
    }

    private tailrec fun collapseDown(nodes: List<Node<T>>, minLevel: Int) {
        val children = nodes
            .asSequence()
            .filterIsInstance<BranchNode<T>>()
            .onEach { if (it.level >= minLevel) it.isExpanded = false }
            .flatMap { it.children }
            .toList()

        if (children.isNotEmpty()) {
            collapseDown(children, minLevel)
        }
    }

    private tailrec fun expandDown(nodes: List<Node<T>>, maxLevel: Int) {
        val children = nodes
            .asSequence()
            .filterIsInstance<BranchNode<T>>()
            .onEach { it.isExpanded = true }
            .filter { it.level < maxLevel }
            .flatMap { it.children }
            .toList()

        if (children.isNotEmpty()) {
            expandDown(children, maxLevel)
        }
    }

    private tailrec fun expandUp(node: Node<T>?) {
        if (node is BranchNode) {
            node.isExpanded = true
            expandUp(node.parent)
        }
    }
}
