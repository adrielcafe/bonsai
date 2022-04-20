package cafe.adriel.bonsai.filesystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.node.Branch
import cafe.adriel.bonsai.core.node.BranchNode
import cafe.adriel.bonsai.core.node.Leaf
import cafe.adriel.bonsai.core.tree.Tree
import cafe.adriel.bonsai.core.tree.TreeScope
import okio.FileSystem
import okio.Path

public fun FileSystemBonsaiStyle(): BonsaiStyle<Path> =
    BonsaiStyle(
        nodeNameStartPadding = 4.dp,
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

@Composable
public fun FileSystemTree(
    rootPath: Path,
    fileSystem: FileSystem,
    selfInclude: Boolean = false
): Tree<Path> =
    Tree {
        FileSystemTree(
            rootPath = rootPath,
            fileSystem = fileSystem,
            selfInclude = selfInclude
        )
    }

@Composable
private fun TreeScope.FileSystemTree(
    rootPath: Path,
    fileSystem: FileSystem,
    selfInclude: Boolean = false
) {
    if (selfInclude) {
        FileSystemNode(rootPath, fileSystem)
    } else {
        fileSystem
            .listOrNull(rootPath)
            ?.forEach { path -> FileSystemNode(path, fileSystem) }
    }
}

@Composable
private fun TreeScope.FileSystemNode(
    path: Path,
    fileSystem: FileSystem
) {
    if (fileSystem.metadata(path).isDirectory) {
        Branch(
            content = path,
            name = path.name
        ) {
            FileSystemTree(path, fileSystem)
        }
    } else {
        Leaf(
            content = path,
            name = path.name
        )
    }
}
