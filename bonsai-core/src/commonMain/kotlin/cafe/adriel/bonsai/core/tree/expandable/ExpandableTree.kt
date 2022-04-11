package cafe.adriel.bonsai.core.tree.expandable

import cafe.adriel.bonsai.core.node.Node

public interface ExpandableTree<T> {

    public fun collapseAll()

    public fun expandAll()

    public fun collapseRoot()

    public fun expandRoot()

    public fun collapseFrom(minLevel: Int)

    public fun expandUntil(maxLevel: Int)

    public fun collapseNode(node: Node<T>)

    public fun expandNode(node: Node<T>, maxLevel: Int = 0)
}
