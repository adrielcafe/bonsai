package cafe.adriel.bonsai.sample.multiplatform

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.bonsai.sample.multiplatform.SampleApplication

public fun main() {
    application {
        Window(onCloseRequest = ::exitApplication) {
            SampleApplication()
        }
    }
}
