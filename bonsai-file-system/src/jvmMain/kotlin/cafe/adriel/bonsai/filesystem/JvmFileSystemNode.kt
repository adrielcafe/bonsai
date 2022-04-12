package cafe.adriel.bonsai.filesystem

import cafe.adriel.bonsai.core.node.Node
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.nio.file.Path as NioPath

public fun fileSystemNodes(
    rootDirectory: File,
    selfInclude: Boolean = false
): List<Node<Path>> =
    fileSystemNodes(
        rootDirectory = rootDirectory.toOkioPath(),
        selfInclude = selfInclude
    )

public fun fileSystemNodes(
    rootDirectory: NioPath,
    selfInclude: Boolean = false
): List<Node<Path>> =
    fileSystemNodes(
        rootDirectory = rootDirectory.toOkioPath(),
        selfInclude = selfInclude
    )
