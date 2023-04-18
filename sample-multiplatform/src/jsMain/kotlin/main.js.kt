import androidx.compose.ui.window.Window
import cafe.adriel.bonsai.sample.multiplatform.SampleApplication
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Sample") {
            SampleApplication()
        }
    }
}
