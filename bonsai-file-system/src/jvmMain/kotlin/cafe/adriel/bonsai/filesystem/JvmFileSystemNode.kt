package cafe.adriel.bonsai.filesystem

import cafe.adriel.bonsai.core.node.Node
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.nio.file.Path as NioPath

public fun fileSystemNodes(
    rootPath: File,
    selfInclude: Boolean = false
): List<Node<Path>> =
    fileSystemNodes(
        rootPath = rootPath.toOkioPath(),
        selfInclude = selfInclude
    )

public fun fileSystemNodes(
    rootPath: NioPath,
    selfInclude: Boolean = false
): List<Node<Path>> =
    fileSystemNodes(
        rootPath = rootPath.toOkioPath(),
        selfInclude = selfInclude
    )
