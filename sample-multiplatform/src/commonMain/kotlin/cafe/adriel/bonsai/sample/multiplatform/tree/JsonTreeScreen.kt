package cafe.adriel.bonsai.sample.multiplatform.tree

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.bonsai.core.Bonsai
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.json.JsonBonsaiStyle
import cafe.adriel.bonsai.json.JsonTree
import cafe.adriel.bonsai.sample.multiplatform.rawJson
import kotlinx.serialization.json.JsonElement

object JsonTreeScreen : TreeScreen<JsonElement> {

    override val title = "JSON Tree"

    @Composable
    override fun composeTree(): Tree<JsonElement> {
        return JsonTree(rawJson)
    }

    @Composable
    override fun BonsaiContent(
        tree: Tree<JsonElement>,
        modifier: Modifier
    ) {
        Bonsai(
            tree = tree,
            style = JsonBonsaiStyle(),
            modifier = modifier
        )
    }
}
