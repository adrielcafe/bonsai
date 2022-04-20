package cafe.adriel.bonsai.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.bonsai.sample.tree.DslTreeScreen
import cafe.adriel.bonsai.sample.tree.FileSystemTreeScreen
import cafe.adriel.bonsai.sample.tree.JsonTreeScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            SampleButton(
                text = "DSL Tree",
                screen = DslTreeScreen
            )
            SampleButton(
                text = "File System Tree",
                screen = FileSystemTreeScreen
            )
            SampleButton(
                text = "JSON Tree",
                screen = JsonTreeScreen
            )
        }
    }

    @Composable
    private fun SampleButton(
        text: String,
        screen: Screen
    ) {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = { navigator push screen }
        ) {
            Text(text)
        }
    }
}
