package cafe.adriel.bonsai.core.tree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.tree.expandable.ExpandableTree
import cafe.adriel.bonsai.core.tree.expandable.ExpandableTreeManager

public class Tree<T>(
    public val nodes: SnapshotStateList<Node<T>>
) : ExpandableTree<T> by ExpandableTreeManager(nodes) {

    public constructor(nodes: List<Node<T>>) :
        this(mutableStateListOf(*nodes.toTypedArray()))
}

@Composable
public fun <T> rememberTree(
    nodes: List<Node<T>>
): Tree<T> =
    rememberSaveable(saver = treeSaver()) {
        Tree(nodes)
    }

private fun <T> treeSaver(): Saver<Tree<T>, Any> =
    listSaver(
        save = { tree -> tree.nodes },
        restore = { nodes -> Tree(nodes) }
    )
