package cafe.adriel.bonsai.sample.tree

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.bonsai.core.Bonsai
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.json.JsonBonsaiStyle
import cafe.adriel.bonsai.json.JsonTree
import cafe.adriel.bonsai.sample.R
import kotlinx.serialization.json.JsonElement
import okio.buffer
import okio.source

object JsonTreeScreen : TreeScreen<JsonElement> {

    override val title = "JSON Tree"

    @Composable
    override fun composeTree(): Tree<JsonElement> {
        val context = LocalContext.current
        val json = context
            .resources
            .openRawResource(R.raw.response)
            .source()
            .buffer()
            .readString(Charsets.UTF_8)

        return JsonTree(json)
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
