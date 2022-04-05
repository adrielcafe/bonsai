package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.Composable
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
)

@Composable
public fun fileSystemNodes(
    rootDirectory: Path,
    fileSystem: FileSystem = FileSystem.SYSTEM,
    selfInclude: Boolean = false,
    style: FileSystemNodeStyle = FileSystemNodeStyle(
        fileIcon = rememberVectorPainter(Icons.Outlined.InsertDriveFile),
        directoryCollapsedIcon = rememberVectorPainter(Icons.Outlined.FolderOpen),
        directoryExpandedIcon = rememberVectorPainter(Icons.Outlined.Folder),
        textStyle = BasicNodeStyle.defaultTextStyle
    )
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
    children = { child -> fileSystemNodes(child, this) },
    style = BasicNodeStyle(
        collapsedIcon = style.directoryCollapsedIcon,
        expandedIcon = style.directoryExpandedIcon,
        textStyle = style.textStyle
    )
)
