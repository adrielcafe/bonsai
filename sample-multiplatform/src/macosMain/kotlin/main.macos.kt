import androidx.compose.ui.window.Window
import cafe.adriel.bonsai.sample.multiplatform.SampleApplication
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    Window("SampleMultiplatform") {
        SampleApplication()
    }
    NSApp?.run()
}

