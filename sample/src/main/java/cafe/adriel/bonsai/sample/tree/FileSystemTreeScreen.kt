package cafe.adriel.bonsai.sample.tree

import android.os.Build
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.bonsai.core.Bonsai
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.filesystem.FileSystemBonsaiStyle
import cafe.adriel.bonsai.filesystem.FileSystemTree
import okio.Path

object FileSystemTreeScreen : TreeScreen<Path> {

    override val title = "File System Tree"

    @Composable
    override fun composeTree(): Tree<Path> {
        val context = LocalContext.current
        val rootDirectory = remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.dataDir
            else context.codeCacheDir
        }

        return FileSystemTree(rootDirectory, selfInclude = true)
    }

    @Composable
    override fun BonsaiContent(
        tree: Tree<Path>,
        modifier: Modifier
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
            modifier = modifier
        )
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
