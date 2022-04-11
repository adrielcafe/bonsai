package cafe.adriel.bonsai.core.tree.selectable

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.bonsai.core.node.Node

public class SelectableTreeManager<T> : SelectableTree<T> {

    override val selectedNodes: MutableList<Node<T>> = mutableStateListOf()

    override fun toggleSelection(node: Node<T>) {
        if (node.isSelected.value) unselectNode(node)
        else selectNode(node)
    }

    override fun selectNode(node: Node<T>) {
        selectedNodes += node
        node.isSelected.value = true
    }

    override fun unselectNode(node: Node<T>) {
        selectedNodes -= node
        node.isSelected.value = false
    }

    override fun clearSelection() {
        selectedNodes
            .onEach { it.isSelected.value = false }
            .clear()
    }
}
