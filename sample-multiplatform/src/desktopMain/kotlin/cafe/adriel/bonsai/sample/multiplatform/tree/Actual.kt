package cafe.adriel.bonsai.sample.multiplatform.tree

import androidx.compose.runtime.Composable
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import java.nio.file.Paths

@Composable
actual fun rootDirectoryPath(): Path {
    return ".".toPath()
}

@Composable
actual fun fileSystem(): FileSystem = FileSystem.SYSTEM
