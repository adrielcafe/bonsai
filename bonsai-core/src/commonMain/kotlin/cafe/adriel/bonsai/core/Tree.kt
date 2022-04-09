package cafe.adriel.bonsai.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
public fun <T> rememberTree(
    rootNodes: List<Node<T>>
): Tree<T> =
    rememberSaveable(saver = treeSaver()) {
        Tree(rootNodes)
    }

private fun <T> treeSaver(): Saver<Tree<T>, Any> =
    listSaver(
        save = { tree -> tree.nodes },
        restore = { nodes -> Tree(nodes) }
    )

public class Tree<T>(
    private val rootNodes: List<Node<T>>
) {

    public val nodes: SnapshotStateList<Node<T>> =
        mutableStateListOf(*rootNodes.toTypedArray())

    public fun collapseAll() {
        collapseDown(rootNodes, minDepth = Int.MAX_VALUE)
    }

    public fun expandAll() {
        expandDown(rootNodes, maxDepth = Int.MAX_VALUE)
    }

    public fun collapseRoot() {
        collapseDown(rootNodes, minDepth = 0)
    }

    public fun expandRoot() {
        expandDown(rootNodes, maxDepth = 0)
    }

    public fun collapseFrom(minDepth: Int) {
        collapseDown(rootNodes, minDepth)
    }

    public fun expandUntil(maxDepth: Int) {
        expandDown(rootNodes, maxDepth)
    }

    public fun collapseNode(node: Node<T>) {
        collapseDown(listOf(node), minDepth = 0)
    }

    public fun expandNode(node: Node<T>, maxDepth: Int = 0) {
        expandUp(node.parent)
        expandDown(listOf(node), maxDepth = maxDepth)
    }

    private tailrec fun collapseDown(nodes: List<Node<T>>, minDepth: Int) {
        val children = nodes
            .asSequence()
            .filterIsInstance<BranchNode<T>>()
            .onEach { if (it.level >= minDepth) it.isExpanded.value = false }
            .flatMap { it.children }
            .toList()

        if (children.isNotEmpty()) {
            collapseDown(children, minDepth)
        }
    }

    private tailrec fun expandDown(nodes: List<Node<T>>, maxDepth: Int) {
        val children = nodes
            .asSequence()
            .filterIsInstance<BranchNode<T>>()
            .onEach { it.isExpanded.value = true }
            .filter { it.level < maxDepth }
            .flatMap { it.children }
            .toList()

        if (children.isNotEmpty()) {
            expandDown(children, maxDepth)
        }
    }

    private tailrec fun expandUp(node: Node<T>?) {
        if (node is BranchNode) {
            node.isExpanded.value = true
            expandUp(node.parent)
        }
    }
}

public interface Node<T> {

    public val content: T

    public val level: Int

    public val parent: Node<T>?

    @Composable
    public fun NodeIcon()

    @Composable
    public fun NodeName()
}

public interface LeafNode<T> : Node<T>

public interface BranchNode<T> : Node<T> {

    public var isExpanded: MutableState<Boolean>

    public val children: SnapshotStateList<Node<T>>
}
