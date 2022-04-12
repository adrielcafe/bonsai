package cafe.adriel.bonsai.sample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Adb
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.bonsai.core.Bonsai
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.core.tree.rememberTree
import cafe.adriel.bonsai.filesystem.FileSystemBonsaiStyle
import cafe.adriel.bonsai.filesystem.fileSystemNodes
import okio.Path

class SampleActivity : ComponentActivity() {

    private val rootDirectory by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) dataDir
        else codeCacheDir
    }

    private val Tree<Path>.firstBranchNodeAtLevel1: Node<Path>
        get() = nodes
            .filterIsInstance<BranchNode<Path>>()
            .first { it.children.isNotEmpty() }
            .children
            .filterIsInstance<BranchNode<Path>>()
            .first { it.children.isNotEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val tree = rememberTree<Path>(
                    nodes = fileSystemNodes(
                        rootDirectory = rootDirectory,
                        selfInclude = true
                    )
                )

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Bonsai(
                        tree = tree,
                        style = FileSystemBonsaiStyle().copy(
                            nodeCollapsedIcon = { node ->
                                getIcon(
                                    path = node.content,
                                    default = if (node is BranchNode) Icons.Outlined.Folder
                                    else Icons.Outlined.InsertDriveFile
                                )
                            },
                            nodeExpandedIcon = { node ->
                                getIcon(path = node.content, default = Icons.Outlined.FolderOpen)
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 4.dp)
                    )
                    TreeController(
                        tree = tree
                    )
                }
            }
        }
    }

    @Composable
    private fun TreeController(
        tree: Tree<Path>
    ) {
        Column {
            TreeManagerTitle("Expand")
            LazyRow {
                item {
                    TreeManagerButton(
                        text = "Root",
                        onClick = { tree.expandRoot() }
                    )
                    TreeManagerButton(
                        text = "Level 2",
                        onClick = { tree.expandUntil(2) }
                    )
                    TreeManagerButton(
                        text = "Node",
                        onClick = { tree.expandNode(tree.firstBranchNodeAtLevel1) }
                    )
                }
            }
            TreeManagerTitle("Collapse")
            LazyRow {
                item {
                    TreeManagerButton(
                        text = "Root",
                        onClick = { tree.collapseRoot() }
                    )
                    TreeManagerButton(
                        text = "Level 2",
                        onClick = { tree.collapseFrom(2) }
                    )
                    TreeManagerButton(
                        text = "Node",
                        onClick = { tree.collapseNode(tree.firstBranchNodeAtLevel1) }
                    )
                }
            }
            TreeManagerTitle("Select")
            LazyRow {
                item {
                    TreeManagerButton(
                        text = "Clear",
                        onClick = { tree.clearSelection() }
                    )
                    TreeManagerButton(
                        text = "Toggle",
                        onClick = { tree.toggleSelection(tree.nodes.first()) }
                    )
                }
            }
        }
    }

    @Composable
    private fun TreeManagerTitle(
        text: String
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }

    @Composable
    private fun TreeManagerButton(
        text: String,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(text = text)
        }
    }

    @Composable
    private fun getIcon(path: Path, default: ImageVector) =
        rememberVectorPainter(
            when (path.toFile().extension) {
                "apk" -> Icons.Outlined.Android
                "jar" -> Icons.Outlined.LocalCafe
                "studio" -> Icons.Outlined.Adb
                "so" -> Icons.Outlined.Memory
                "xml" -> Icons.Outlined.Description
                "png", "webp", "jpg" -> Icons.Outlined.Image
                "mp4", "webm", "gif" -> Icons.Outlined.Videocam
                "wav", "mp3", "ogg" -> Icons.Outlined.Mic
                else -> default
            }
        )
}
