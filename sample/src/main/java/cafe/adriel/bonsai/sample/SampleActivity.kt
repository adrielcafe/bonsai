package cafe.adriel.bonsai.sample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.bonsai.core.Tree
import cafe.adriel.bonsai.core.TreeStyle
import cafe.adriel.bonsai.filesystem.FileSystemNodeStyle
import cafe.adriel.bonsai.filesystem.fileSystemNodes
import okio.Path

class SampleActivity : ComponentActivity() {

    private val rootDirectory by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) dataDir
        else codeCacheDir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column {
                    Tree(
                        nodes = fileSystemNodes(
                            rootDirectory = rootDirectory,
                            selfInclude = true,
                            style = FileSystemNodeStyle.DefaultStyle.copy(
                                fileIcon = { getIcon(path = it, default = Icons.Outlined.InsertDriveFile) },
                                directoryCollapsedIcon = { getIcon(path = it, default = Icons.Outlined.Folder) },
                                directoryExpandedIcon = { getIcon(path = it, default = Icons.Outlined.FolderOpen) },
                            )
                        ),
                        style = TreeStyle(
                            toggleIcon = rememberVectorPainter(Icons.Default.ChevronRight)
                        ),
                        onClick = { node, state ->
                            println("CLICK $state , ${node.content}")
                            true
                        },
                        onLongClick = { node, state ->
                            println("LONG CLICK $state , ${node.content}")
                            false
                        },
                        onDoubleClick = { node, state ->
                            println("DOUBLE CLICK $state , ${node.content}")
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
