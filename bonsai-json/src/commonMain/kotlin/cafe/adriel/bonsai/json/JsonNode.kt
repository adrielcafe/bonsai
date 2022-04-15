package cafe.adriel.bonsai.json

import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.node.SimpleBranchNode
import cafe.adriel.bonsai.core.node.SimpleLeafNode
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

public fun jsonNodes(
    json: String
): List<Node<JsonElement>> =
    jsonNodes(
        key = "",
        jsonElement = Json.Default.parseToJsonElement(json),
        parent = null
    )

private fun jsonNodes(
    key: String,
    jsonElement: JsonElement,
    parent: Node<JsonElement>?
): List<Node<JsonElement>> =
    listOf(
        when (jsonElement) {
            is JsonNull -> JsonPrimitiveNode(key, jsonElement, parent)
            is JsonPrimitive -> JsonPrimitiveNode(key, jsonElement, parent)
            is JsonObject -> JsonObjectNode(key, jsonElement, parent)
            is JsonArray -> JsonArrayNode(key, jsonElement, parent)
        }
    )

private fun JsonPrimitiveNode(
    key: String,
    jsonPrimitive: JsonPrimitive,
    parent: Node<JsonElement>?
) =
    SimpleLeafNode(
        content = jsonPrimitive,
        name = "${getFormattedKey(key)}${getFormattedValue(jsonPrimitive)}",
        parent = parent
    )

private fun JsonObjectNode(
    key: String,
    jsonObject: JsonObject,
    parent: Node<JsonElement>?
) =
    SimpleBranchNode(
        content = jsonObject,
        name = "${getFormattedKey(key)}{object}",
        parent = parent,
        children = { node ->
            jsonObject.entries.flatMap { (name, jsonElement) ->
                jsonNodes(name, jsonElement, node)
            }
        }
    )

private fun JsonArrayNode(
    key: String,
    jsonArray: JsonArray,
    parent: Node<JsonElement>?
) =
    SimpleBranchNode(
        content = jsonArray,
        name = "${getFormattedKey(key)}[array]",
        parent = parent,
        children = { node ->
            jsonArray.flatMapIndexed { index, jsonElement ->
                jsonNodes(index.toString(), jsonElement, node)
            }
        }
    )

private fun getFormattedKey(key: String) =
    if (key.isBlank()) ""
    else "$key: "

private fun getFormattedValue(jsonPrimitive: JsonPrimitive) =
    if (jsonPrimitive.isString) "\"${jsonPrimitive.contentOrNull}\""
    else jsonPrimitive.contentOrNull
