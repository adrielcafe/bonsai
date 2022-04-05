package cafe.adriel.bonsai.sample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.bonsai.core.Tree
import cafe.adriel.bonsai.core.TreeStyle
import cafe.adriel.bonsai.filesystem.fileSystemNodes
import okio.Path.Companion.toOkioPath

class SampleActivity : ComponentActivity() {

    private val rootDirectory by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) dataDir
        else codeCacheDir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column {
                    Tree(
                        nodes = fileSystemNodes(
                            rootDirectory = rootDirectory.toOkioPath(),
                            selfInclude = true
                        ),
                        style = TreeStyle(
                            toggleIcon = rememberVectorPainter(Icons.Default.ChevronRight)
                        ),
                        onClick = { node, state ->
                            println("CLICK $state , ${node.content}")
                            true
                        },
                        onLongClick = { node, state ->
                            println("LONG CLICK $state , ${node.content}")
                            false
                        },
                        onDoubleClick = { node, state ->
                            println("DOUBLE CLICK $state , ${node.content}")
                            false
                        }
                    )
                }
            }
        }
    }
}
