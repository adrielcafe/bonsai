package cafe.adriel.bonsai.core.tree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.tree.expandable.ExpandableTree
import cafe.adriel.bonsai.core.tree.expandable.ExpandableTreeManager

public class Tree<T> internal constructor(
    public val nodes: MutableList<Node<T>>
) : ExpandableTree<T> by ExpandableTreeManager(nodes)

@Composable
public fun <T> rememberTree(
    nodes: List<Node<T>>
): Tree<T> =
    rememberSaveable(saver = treeSaver()) {
        Tree(nodes.toMutableStateList())
    }

private fun <T> treeSaver(): Saver<Tree<T>, Any> =
    listSaver(
        save = { tree -> tree.nodes },
        restore = { nodes -> Tree(nodes.toMutableList()) }
    )
