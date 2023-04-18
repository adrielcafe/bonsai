package cafe.adriel.bonsai.sample.multiplatform

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun SampleApplication() {
    MaterialTheme {
        Navigator(HomeScreen)
    }
}
