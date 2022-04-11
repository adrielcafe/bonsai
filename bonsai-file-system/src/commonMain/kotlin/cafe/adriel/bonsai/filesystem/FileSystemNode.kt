package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import cafe.adriel.bonsai.core.node.BasicBranchNode
import cafe.adriel.bonsai.core.node.BasicLeafNode
import cafe.adriel.bonsai.core.node.BasicNodeStyle
import cafe.adriel.bonsai.core.node.Node
import okio.FileSystem
import okio.Path

public typealias FileSystemIcon = @Composable (Path) -> Painter?

public data class FileSystemNodeScope(
    val fileSystem: FileSystem = FileSystem.SYSTEM,
    val style: FileSystemNodeStyle = FileSystemNodeStyle.DefaultStyle
)

public data class FileSystemNodeStyle(
    val fileIcon: FileSystemIcon,
    val directoryCollapsedIcon: FileSystemIcon,
    val directoryExpandedIcon: FileSystemIcon,
    val textStyle: TextStyle
) {

    public companion object {
        public val DefaultStyle: FileSystemNodeStyle = FileSystemNodeStyle(
            fileIcon = { rememberVectorPainter(Icons.Outlined.InsertDriveFile) },
            directoryCollapsedIcon = { rememberVectorPainter(Icons.Outlined.Folder) },
            directoryExpandedIcon = { rememberVectorPainter(Icons.Outlined.FolderOpen) },
            textStyle = BasicNodeStyle.DefaultTextStyle
        )
    }
}

public fun fileSystemNodes(
    rootDirectory: Path,
    selfInclude: Boolean = false,
    scope: FileSystemNodeScope = FileSystemNodeScope()
): List<Node<Path>> =
    with(scope) {
        fileSystemNodes(
            rootDirectory = rootDirectory,
            level = 0,
            parent = null,
            selfInclude = selfInclude,
        )
    }

private fun FileSystemNodeScope.fileSystemNodes(
    rootDirectory: Path,
    level: Int,
    parent: Node<Path>?,
    selfInclude: Boolean = false
): List<Node<Path>> =
    if (selfInclude) {
        listOf(directoryNode(rootDirectory, level, parent))
    } else {
        fileSystem
            .listOrNull(rootDirectory)
            ?.map { path ->
                if (fileSystem.metadata(path).isDirectory) {
                    directoryNode(path, level, parent)
                } else {
                    fileNode(path, level, parent)
                }
            }
            .orEmpty()
    }

private fun FileSystemNodeScope.fileNode(
    file: Path,
    level: Int,
    parent: Node<Path>?
) = BasicLeafNode(
    content = file,
    name = file.name,
    level = level,
    parent = parent,
    style = BasicNodeStyle(
        collapsedIcon = { style.fileIcon(file) },
        textStyle = style.textStyle
    )
)

private fun FileSystemNodeScope.directoryNode(
    directory: Path,
    level: Int,
    parent: Node<Path>?
) = BasicBranchNode(
    content = directory,
    name = directory.name,
    level = level,
    parent = parent,
    children = mutableStateListOf(),
    style = BasicNodeStyle(
        collapsedIcon = { style.directoryCollapsedIcon(directory) },
        expandedIcon = { style.directoryExpandedIcon(directory) },
        textStyle = style.textStyle
    )
).apply {
    children.addAll(fileSystemNodes(directory, level.inc(), this))
}
