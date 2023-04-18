package cafe.adriel.bonsai.sample.multiplatform.tree

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath

@Composable
actual fun rootDirectoryPath(): Path {
    val context = LocalContext.current
    return remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.dataDir
        else context.codeCacheDir
    }.toOkioPath()
}

@Composable
actual fun fileSystem(): FileSystem = FileSystem.SYSTEM

