package cafe.adriel.bonsai.sample.multiplatform.tree

import androidx.compose.runtime.Composable
import okio.FileSystem
import okio.Path

@Composable
actual fun rootDirectoryPath(): Path {
     TODO()
}

@Composable
actual fun fileSystem(): FileSystem = FileSystem.SYSTEM // ??? isso mesmo???
