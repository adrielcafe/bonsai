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
    rootPath: Path,
    selfInclude: Boolean = false,
    fileSystem: FileSystem = FileSystem.SYSTEM
): List<Node<Path>> =
    with(
        FileSystemNodeScope(
            fileSystem = fileSystem
        )
    ) {
        fileSystemNodes(
            rootPath = rootPath,
            parent = null,
            selfInclude = selfInclude,
        )
    }

private fun FileSystemNodeScope.fileSystemNodes(
    rootPath: Path,
    parent: Node<Path>?,
    selfInclude: Boolean = false
): List<Node<Path>> =
    if (selfInclude) {
        listOf(FileSystemNode(rootPath, parent))
    } else {
        fileSystem
            .listOrNull(rootPath)
            ?.map { path -> FileSystemNode(path, parent) }
            .orEmpty()
    }

private fun FileSystemNodeScope.FileSystemNode(
    path: Path,
    parent: Node<Path>?
) =
    if (fileSystem.metadata(path).isDirectory) {
        SimpleBranchNode(
            content = path,
            name = path.name,
            parent = parent,
            children = { node -> fileSystemNodes(path, node) }
        )
    } else {
        SimpleLeafNode(
            content = path,
            name = path.name,
            parent = parent
        )
    }
