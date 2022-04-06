package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import cafe.adriel.bonsai.core.BasicBranchNode
import cafe.adriel.bonsai.core.BasicLeafNode
import cafe.adriel.bonsai.core.BasicNodeStyle
import cafe.adriel.bonsai.core.Node
import okio.FileSystem
import okio.Path

private data class FileSystemNodeScope(
    val fileSystem: FileSystem,
    val style: FileSystemNodeStyle
)

public data class FileSystemNodeStyle(
    val fileIcon: Painter?,
    val directoryCollapsedIcon: Painter?,
    val directoryExpandedIcon: Painter?,
    val textStyle: TextStyle
) {

    public companion object {
        public val DefaultStyle: FileSystemNodeStyle
            @Composable
            get() {
                val fileIcon = rememberVectorPainter(Icons.Outlined.InsertDriveFile)
                val directoryCollapsedIcon = rememberVectorPainter(Icons.Outlined.Folder)
                val directoryExpandedIcon = rememberVectorPainter(Icons.Outlined.FolderOpen)

                return remember {
                    FileSystemNodeStyle(
                        fileIcon = fileIcon,
                        directoryCollapsedIcon = directoryCollapsedIcon,
                        directoryExpandedIcon = directoryExpandedIcon,
                        textStyle = BasicNodeStyle.DefaultTextStyle
                    )
                }
            }
    }
}

@Composable
public fun fileSystemNodes(
    rootDirectory: Path,
    fileSystem: FileSystem = FileSystem.SYSTEM,
    selfInclude: Boolean = false,
    style: FileSystemNodeStyle = FileSystemNodeStyle.DefaultStyle
): List<Node<Path>> =
    with(FileSystemNodeScope(fileSystem, style)) {
        fileSystemNodes(
            rootDirectory = rootDirectory,
            selfInclude = selfInclude,
        )
    }

private fun FileSystemNodeScope.fileSystemNodes(
    rootDirectory: Path,
    selfInclude: Boolean = false,
): List<Node<Path>> =
    if (selfInclude) {
        listOf(directoryNode(rootDirectory))
    } else {
        fileSystem
            .listOrNull(rootDirectory)
            ?.map { path ->
                if (FileSystem.SYSTEM.metadata(path).isDirectory) {
                    directoryNode(path)
                } else {
                    fileNode(path)
                }
            }
            .orEmpty()
    }

private fun FileSystemNodeScope.fileNode(
    file: Path
) = BasicLeafNode(
    content = file,
    name = file.name,
    style = BasicNodeStyle(
        collapsedIcon = style.fileIcon,
        textStyle = style.textStyle
    )
)

private fun FileSystemNodeScope.directoryNode(
    directory: Path
) = BasicBranchNode(
    content = directory,
    name = directory.name,
    children = { child -> fileSystemNodes(child) },
    style = BasicNodeStyle(
        collapsedIcon = style.directoryCollapsedIcon,
        expandedIcon = style.directoryExpandedIcon,
        textStyle = style.textStyle
    )
)
