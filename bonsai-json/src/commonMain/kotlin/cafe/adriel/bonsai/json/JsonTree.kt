package cafe.adriel.bonsai.json

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.node.Branch
import cafe.adriel.bonsai.core.node.Leaf
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.core.tree.TreeScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

public fun JsonBonsaiStyle(): BonsaiStyle<JsonElement> =
    BonsaiStyle(
        nodeNameTextStyle = BonsaiStyle.DefaultNodeTextStyle.copy(
            fontFamily = FontFamily.Monospace
        )
    )

@Composable
public fun JsonTree(
    json: String
): Tree<JsonElement> =
    Tree {
        JsonNode(
            key = null,
            jsonElement = Json.Default.parseToJsonElement(json)
        )
    }

@Composable
private fun TreeScope.JsonNode(
    key: String?,
    jsonElement: JsonElement
) {
    when (jsonElement) {
        is JsonNull -> JsonPrimitiveNode(key, jsonElement)
        is JsonPrimitive -> JsonPrimitiveNode(key, jsonElement)
        is JsonObject -> JsonObjectNode(key, jsonElement)
        is JsonArray -> JsonArrayNode(key, jsonElement)
    }
}

@Composable
private fun TreeScope.JsonPrimitiveNode(
    key: String?,
    jsonPrimitive: JsonPrimitive
) {
    Leaf(
        content = jsonPrimitive,
        name = "${getFormattedKey(key)}${getFormattedValue(jsonPrimitive)}",
    )
}

@Composable
private fun TreeScope.JsonObjectNode(
    key: String?,
    jsonObject: JsonObject
) {
    Branch(
        content = jsonObject,
        name = "${getFormattedKey(key)}{object}"
    ) {
        jsonObject.entries.forEach { (key, jsonElement) ->
            JsonNode(key, jsonElement)
        }
    }
}

@Composable
private fun TreeScope.JsonArrayNode(
    key: String?,
    jsonArray: JsonArray
) {
    Branch(
        content = jsonArray,
        name = "${getFormattedKey(key)}[array]"
    ) {
        jsonArray.forEachIndexed { index, jsonElement ->
            JsonNode(index.toString(), jsonElement)
        }
    }
}

private fun getFormattedKey(key: String?) =
    if (key.isNullOrBlank()) ""
    else "$key: "

private fun getFormattedValue(jsonPrimitive: JsonPrimitive) =
    if (jsonPrimitive.isString) "\"${jsonPrimitive.contentOrNull}\""
    else jsonPrimitive.contentOrNull
