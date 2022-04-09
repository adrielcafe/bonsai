package cafe.adriel.bonsai.sample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.bonsai.core.BranchNode
import cafe.adriel.bonsai.core.Node
import cafe.adriel.bonsai.core.Tree
import cafe.adriel.bonsai.core.component.Tree
import cafe.adriel.bonsai.core.component.TreeStyle
import cafe.adriel.bonsai.filesystem.FileSystemNodeStyle
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
                val tree = remember {
                    Tree<Path>(
                        rootNodes = fileSystemNodes(
                            rootDirectory = rootDirectory,
                            selfInclude = true,
                            style = FileSystemNodeStyle.DefaultStyle.copy(
                                fileIcon = { getIcon(path = it, default = Icons.Outlined.InsertDriveFile) },
                                directoryCollapsedIcon = { getIcon(path = it, default = Icons.Outlined.Folder) },
                                directoryExpandedIcon = { getIcon(path = it, default = Icons.Outlined.FolderOpen) },
                            )
                        )
                    )
                }

                Column {
                    Row {
                        Button(onClick = { tree.collapseRoot() }) {
                            Text(text = "Collapse Root")
                        }
                        Button(onClick = { tree.expandRoot() }) {
                            Text(text = "Expand Root")
                        }
                    }
                    Row {
                        Button(onClick = { tree.collapseFrom(2) }) {
                            Text(text = "Collapse Level 2")
                        }
                        Button(onClick = { tree.expandUntil(2) }) {
                            Text(text = "Expand Level 2")
                        }
                    }
                    Row {
                        Button(onClick = { tree.collapseNode(tree.firstBranchNodeAtLevel1) }) {
                            Text(text = "Collapse Node")
                        }
                        Button(onClick = { tree.expandNode(tree.firstBranchNodeAtLevel1) }) {
                            Text(text = "Expand Node")
                        }
                    }
                    Tree(
                        tree = tree,
                        style = TreeStyle(
                            toggleIcon = rememberVectorPainter(Icons.Default.ChevronRight)
                        ),
                        onClick = { node ->
                            println("CLICK ${node.content}")
                            true
                        },
                        onLongClick = { node ->
                            println("LONG CLICK ${node.content}")
                            false
                        },
                        onDoubleClick = { node ->
                            println("DOUBLE ${node.content}")
                            false
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun getIcon(path: Path, default: ImageVector) =
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
        }.let { rememberVectorPainter(it) }
}
