package cafe.adriel.bonsai.sample.multiplatform.tree

import androidx.compose.runtime.Composable
import okio.FileSystem
import okio.Path

@Composable
expect fun rootDirectoryPath(): Path

@Composable
expect fun fileSystem(): FileSystem
