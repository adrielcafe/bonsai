package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.LeafNode
import cafe.adriel.bonsai.core.node.Node
import okio.FileSystem
import okio.Path

internal data class FileSystemNodeScope(
    val fileSystem: FileSystem
)

public fun FileSystemBonsaiStyle(): BonsaiStyle<Path> =
    BonsaiStyle(
        nodeCollapsedIcon = { node ->
            rememberVectorPainter(
                if (node is BranchNode) Icons.Outlined.Folder
                else Icons.Outlined.InsertDriveFile
            )
        },
        nodeExpandedIcon = {
            rememberVectorPainter(Icons.Outlined.FolderOpen)
        }
    )

public fun fileSystemNodes(
    rootDirectory: Path,
    selfInclude: Boolean = false,
    fileSystem: FileSystem = FileSystem.SYSTEM
): List<Node<Path>> =
    with(
        FileSystemNodeScope(
            fileSystem = fileSystem
        )
    ) {
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
        listOf(DirectoryNode(rootDirectory, level, parent))
    } else {
        fileSystem
            .listOrNull(rootDirectory)
            ?.map { path ->
                if (fileSystem.metadata(path).isDirectory) {
                    DirectoryNode(path, level, parent)
                } else {
                    FileNode(path, level, parent)
                }
            }
            .orEmpty()
    }

private fun FileSystemNodeScope.FileNode(
    file: Path,
    level: Int,
    parent: Node<Path>?
) = LeafNode(
    content = file,
    name = file.name,
    level = level,
    parent = parent
)

private fun FileSystemNodeScope.DirectoryNode(
    directory: Path,
    level: Int,
    parent: Node<Path>?
) = BranchNode(
    content = directory,
    name = directory.name,
    level = level,
    parent = parent,
    children = mutableStateListOf()
).apply {
    children.addAll(fileSystemNodes(directory, level.inc(), this))
}
