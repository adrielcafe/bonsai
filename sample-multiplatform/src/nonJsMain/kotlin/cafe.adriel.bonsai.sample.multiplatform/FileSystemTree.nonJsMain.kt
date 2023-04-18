package cafe.adriel.bonsai.sample.multiplatform

import cafe.adriel.bonsai.sample.multiplatform.tree.FileSystemTreeScreen
import cafe.adriel.voyager.core.screen.Screen

actual fun FileSystemTreeScreenOrNull(): Screen? = FileSystemTreeScreen
