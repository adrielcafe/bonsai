package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Node
import cafe.adriel.bonsai.core.node.SimpleBranchNode
import cafe.adriel.bonsai.core.node.SimpleLeafNode
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
            parent = null,
            selfInclude = selfInclude,
        )
    }

private fun FileSystemNodeScope.fileSystemNodes(
    rootDirectory: Path,
    parent: Node<Path>?,
    selfInclude: Boolean = false
): List<Node<Path>> =
    if (selfInclude) {
        listOf(DirectoryNode(rootDirectory, parent))
    } else {
        fileSystem
            .listOrNull(rootDirectory)
            ?.map { path ->
                if (fileSystem.metadata(path).isDirectory) {
                    DirectoryNode(path, parent)
                } else {
                    FileNode(path, parent)
                }
            }
            .orEmpty()
    }

private fun FileSystemNodeScope.FileNode(
    file: Path,
    parent: Node<Path>?
) = SimpleLeafNode(
    content = file,
    name = file.name,
    parent = parent
)

private fun FileSystemNodeScope.DirectoryNode(
    directory: Path,
    parent: Node<Path>?
) = SimpleBranchNode(
    content = directory,
    name = directory.name,
    parent = parent,
    children = { node -> fileSystemNodes(directory, node) }
)
